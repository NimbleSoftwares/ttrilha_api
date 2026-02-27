# Error Handling Standards

This document defines how errors and exceptions are handled across the TTrilha API. The goal is to provide a consistent, secure, and maintainable error-handling strategy that scales as the application grows.
# HOW TO HANDLE ERRORS IN TTRILHA API

## Exception Hierarchy

All custom exceptions extend one of two abstract base classes, establishing the **layered error strategy**:

```
RuntimeException
├── DomainException (abstract)
│   ├── InvalidProviderException
│   ├── UserNotFoundException
│   └── [other domain rule violations]
│
└── ApplicationException (abstract)
    ├── UserCreationFailedException
    ├── UserIdentityPersistenceException
    └── [other orchestration/use-case errors]
```

### DomainException

- **Where defined**: `domain/{aggregate}/exception/`
- **What it represents**: Violations of business rules or invariants
- **Who throws it**: Domain models, value objects, use cases
- **HTTP mapping**: Typically 4xx (400, 404, 409)
- **Examples**:
  - `InvalidProviderException` — unsupported OAuth provider
  - `UserNotFoundException` — user lookup returned no match
  - `UserAlreadyExistsException` — user identity already exists (business rule)

### ApplicationException

- **Where defined**: `application/{aggregate}/exception/`
- **What it represents**: Orchestration/use-case level failures, infrastructure issues
- **Who throws it**: Use case services, persistence adapters
- **HTTP mapping**: Typically 4xx or 5xx depending on subtype
- **Examples**:
  - `UserCreationFailedException` — persistence layer failed unexpectedly
  - `UserIdentityPersistenceException` — database system error

---

## Where Exceptions Are Thrown

### Domain Layer

Throw `DomainException` subtypes when business rules are violated:

### Application Layer (Use Case Services)

Throw `DomainException` or `ApplicationException` depending on the error type:

```java
// application/user/service/CreateUserService.java
@Override
public UUID execute(CreateUserCommand command) {
  Optional<UserIdentity> existing = userIdentityRepository.findByProviderAndProviderUserId(
    command.provider(),
    command.providerUserId()
  );

  if (existing.isPresent()) {
    return existing.get().getUser().getId();
  }

  // If domain rule is violated, throws DomainException subtype
  User savedUser = userRepository.save(command.toUser());
  userIdentityRepository.save(command.toUserIdentity(savedUser));

  return savedUser.getId();
}
```

**Rule**: No try-catch in use cases. Let exceptions bubble up to the global handler.

### Adapter Layer (Persistence)

**This is the ONLY place where try-catch is acceptable and necessary.** The adapter catches infrastructure-specific exceptions and translates them to your typed exceptions before they leak through the port boundary.

```java
// adapter/out/persistence/user/impl/UserIdentityJpaRepositoryAdapter.java
public UserIdentity save(UserIdentity userIdentity) {
  UserIdentityEntity entity = mapper.toPersistence(userIdentity);

  try {
    UserIdentityEntity saved = userIdentityJpaRepository.save(entity);
    return mapper.toDomain(saved);
  } catch (DataIntegrityViolationException e) {
    // Translate JPA exception to domain exception
    log.error("User identity already exists", e);
    throw new UserIdentityAlreadyExistsException(
      "This identity is already registered."
    );
  } catch (JpaSystemException e) {
    // Translate JPA system error to application exception
    log.error("Database system error while saving user identity", e);
    throw new UserIdentityPersistenceException(
      "An error occurred while saving. Please try again."
    );
  }
}
```

**Rules**:
- Catch only infrastructure-specific exceptions (JPA, HTTP client, etc.)
- **Never let these leak through the port interface** — always re-throw as your typed exception
- Log the full exception with stack trace for debugging

### Web Controller

**No try-catch here.** Controllers just call use cases and let exceptions bubble to the global handler:

```java
// adapter/in/web/user/UserController.java
@PostMapping
public ResponseEntity<CreateUserResponse> create(
  @Valid @RequestBody CreateUserRequest request,
  @AuthenticationPrincipal Jwt jwt
) {
  String sub = jwt.getClaimAsString("sub");
  CreateUserCommand command = request.toCommand(sub);

  UUID userId = createUserUseCase.execute(command);  // might throw, that's OK

  return ResponseEntity.status(201).body(new CreateUserResponse(userId));
}
```

---

## Where Exceptions Are Caught

**One place only: `@RestControllerAdvice`**

All exceptions (domain, application, Spring's own) are caught in a single global exception handler living in the web adapter layer.

### Handler Location

`adapter/in/web/exception/GlobalExceptionHandler.java`

Or, as the application grows and you add more domain aggregates:

```
adapter/in/web/exception/
├── GlobalExceptionHandler.java         (generic/fallback)
├── user/
│   └── UserExceptionHandler.java       (@RestControllerAdvice for user errors)
├── product/
│   └── ProductExceptionHandler.java    (@RestControllerAdvice for product errors)
└── order/
    └── OrderExceptionHandler.java      (@RestControllerAdvice for order errors)
```

Each handler is focused and small. Spring routes each exception to the most specific handler that can handle it.

---

## Response Format: RFC 9457 ProblemDetail

All error responses follow the **RFC 9457 standard** (Problem Details for HTTP APIs), implemented via Spring Boot's `ProblemDetail` class.

### Enable RFC 9457 in application.properties

```properties
spring.mvc.problemdetails.enabled=true
```

This makes Spring automatically convert its own exceptions (like `MethodArgumentNotValidException`) to RFC 9457 format.

### Response Structure

```json
{
  "type": "https://api.example.com/errors/identity-already-exists",
  "title": "Identity Already Exists",
  "status": 409,
  "detail": "This identity is already registered.",
  "instance": "/api/v1/users",
  "traceId": "5f2d8e1a9b4c7d2e"
}
```

| Field | Purpose |
|---|---|
| `type` | A URI identifying the error category (optional but recommended) |
| `title` | Short, human-readable error title |
| `status` | HTTP status code |
| `detail` | Specific error detail message |
| `instance` | The request URI that caused the error |
| `traceId` | Unique request identifier for correlation with server logs |

### Building a ProblemDetail in Your Handler

```java
@ExceptionHandler(UserIdentityAlreadyExistsException.class)
public ProblemDetail handle(UserIdentityAlreadyExistsException e) {
  String traceId = getTraceId();

  ProblemDetail detail = ProblemDetail.forStatusAndDetail(
    HttpStatus.CONFLICT,
    e.getMessage()
  );
  detail.setTitle("Identity Already Exists");
  detail.setProperty("traceId", traceId);

  return detail;
}
```

---

## Trace ID for Error Correlation

A **trace ID** is a unique identifier for each HTTP request. It appears in all logs related to that request and is returned in the error response, allowing developers and support to correlate logs with client-reported errors.

### How It Works

1. Each HTTP request gets a unique trace ID (auto-generated via Micrometer Tracing)
2. The trace ID is automatically injected into all logs for that request
3. If an error occurs, the trace ID is included in the ProblemDetail response
4. Client can report: "My request with trace ID `abc123` failed"
5. You search logs for `traceId: abc123` to find what went wrong

### Generate and Inject Trace ID

Use `Tracer` from Micrometer Tracing (already added to `pom.xml`):

```java
import io.micrometer.tracing.Tracer;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Tracer tracer;

  public GlobalExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  private String getTraceId() {
    try {
        return Objects.requireNonNull(tracer.currentSpan()).context().traceId();
    } catch (Exception e) {
      log.debug("Could not extract trace ID", e);
    }
    return UUID.randomUUID().toString();
  }

  @ExceptionHandler(UserIdentityAlreadyExistsException.class)
  public ProblemDetail handle(UserIdentityAlreadyExistsException e) {
    String traceId = getTraceId();

    ProblemDetail detail = ProblemDetail.forStatusAndDetail(
      HttpStatus.CONFLICT,
      e.getMessage()
    );
    detail.setTitle("Identity Already Exists");
    detail.setProperty("traceId", traceId);

    return detail;
  }
}
```

### Configure Logging to Include Trace ID

In `logback.xml`:

```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss} [%X{traceId}] [%-5level] %logger{36} - %msg%n</pattern>
```

Result in logs:
```
2026-02-26 10:30:45 [5f2d8e1a9b4c7d2e] [ERROR] GlobalExceptionHandler - User identity conflict
```

---

## Error Messages: Security & UX

### Never Expose Infrastructure Details

```java
// ❌ WRONG - exposes database constraints
catch (DataIntegrityViolationException e) {
  throw new UserIdentityAlreadyExistsException(e.getMessage());
  // Client sees: "Duplicate entry 'google|12345' for key 'user_identity.uk_provider_user_id'"
}

// ✅ CORRECT - generic, friendly message
catch (DataIntegrityViolationException e) {
  log.error("User identity already exists", e);  // ← stack trace in logs
  throw new UserIdentityAlreadyExistsException("This identity is already registered.");  // ← generic message
}
```

### Rules for Error Messages

1. **In Exception Constructor**: Pass a **generic, user-friendly message** that doesn't expose infrastructure
2. **In Logs**: Always log the original exception with full stack trace for debugging
3. **In HTTP Response**: Return the generic message from the exception — never expose stack traces to clients

### Examples of Good Error Messages

```java
throw new UserIdentityAlreadyExistsException("This identity is already registered.");
throw new InvalidProviderException("Provider is not supported.");
throw new UserCreationFailedException("An error occurred while creating the user. Please try again.");
throw new UserNotFoundException("No user found with the given credentials.");
```

---

## Implementation Checklist

When adding a new error case to the application:

- [ ] **Identify the error type**: Is it a domain rule violation (DomainException) or orchestration failure (ApplicationException)?
- [ ] **Create the exception class** in the appropriate package:
  - `domain/{aggregate}/exception/{ExceptionName}.java` for domain errors
  - `application/{aggregate}/exception/{ExceptionName}.java` for application errors
- [ ] **Extend the correct base class**: `DomainException` or `ApplicationException`
- [ ] **Pass a generic message** to the constructor (no infrastructure details)
- [ ] **Throw it** from the appropriate layer (domain, use case, or adapter)
- [ ] **Add an @ExceptionHandler** in the appropriate `*ExceptionHandler` class in `adapter/in/web/exception/`
- [ ] **Return a ProblemDetail** with:
  - The correct HTTP status
  - A clear title and detail message
  - The trace ID
- [ ] **Log the exception** in the adapter (if translating from JPA)
- [ ] **Test** both the happy path and the error case with integration tests

---

## Examples

### Example 1: Domain Exception (InvalidProviderException)

**Where it's thrown:**
```java
// domain/user/enums/ProviderEnum.java
public static ProviderEnum fromProviderSubPrefix(String prefix) {
  return Arrays.stream(values())
    .filter(p -> p.subPrefix.equals(prefix))
    .findFirst()
    .orElseThrow(() -> new InvalidProviderException(
      "Provider '" + prefix + "' is not supported."
    ));
}
```

**Where it's caught and handled:**
```java
// adapter/in/web/exception/UserExceptionHandler.java
@ExceptionHandler(InvalidProviderException.class)
public ProblemDetail handleInvalidProvider(InvalidProviderException e) {
  String traceId = getTraceId();

  ProblemDetail detail = ProblemDetail.forStatusAndDetail(
    HttpStatus.BAD_REQUEST,
    e.getMessage()
  );
  detail.setTitle("Invalid Provider");
  detail.setProperty("traceId", traceId);

  return detail;
}
```

**Client response (400):**
```json
{
  "type": "about:blank",
  "title": "Invalid Provider",
  "status": 400,
  "detail": "Provider 'twitter' is not supported.",
  "instance": "/api/v1/users",
  "traceId": "5f2d8e1a9b4c7d2e"
}
```

### Example 2: Application Exception (UserIdentityPersistenceException)

**Where it's thrown (in adapter):**
```java
// adapter/out/persistence/user/impl/UserIdentityJpaRepositoryAdapter.java
public UserIdentity save(UserIdentity userIdentity) {
  UserIdentityEntity entity = mapper.toPersistence(userIdentity);

  try {
    UserIdentityEntity saved = userIdentityJpaRepository.save(entity);
    return mapper.toDomain(saved);
  } catch (JpaSystemException e) {
    log.error("Database system error while saving user identity", e);  // ← full exception logged
    throw new UserIdentityPersistenceException(
      "An error occurred while saving. Please try again."  // ← generic message
    );
  }
}
```

**Where it's caught and handled:**
```java
// adapter/in/web/exception/UserExceptionHandler.java
@ExceptionHandler(UserIdentityPersistenceException.class)
public ProblemDetail handleUserIdentityPersistence(UserIdentityPersistenceException e) {
  String traceId = getTraceId();

  ProblemDetail detail = ProblemDetail.forStatusAndDetail(
    HttpStatus.INTERNAL_SERVER_ERROR,
    e.getMessage()
  );
  detail.setTitle("An Error Has Occurred");
  detail.setProperty("traceId", traceId);

  return detail;
}
```

**Client response (500):**
```json
{
  "type": "about:blank",
  "title": "An Error Has Occurred",
  "status": 500,
  "detail": "An error occurred while saving. Please try again.",
  "instance": "/api/v1/users",
  "traceId": "5f2d8e1a9b4c7d2e"
}
```

**Server logs:**
```
2026-02-26 10:30:45 [5f2d8e1a9b4c7d2e] [ERROR] UserIdentityJpaRepositoryAdapter - Database system error while saving user identity
org.springframework.orm.jpa.JpaSystemException: Could not execute statement
  at org.hibernate.orm.jdbc.internal.JdbcCoordinatorImpl.logicalConnection()
  ...
```

### Example 3: Multiple @ExceptionHandler Methods in One Handler

```java
// adapter/in/web/exception/UserExceptionHandler.java
@RestControllerAdvice
public class UserExceptionHandler {

  private final Tracer tracer;

  public UserExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  private String getTraceId() {
    try {
      if (tracer.currentSpan() != null && tracer.currentSpan().context() != null) {
        return tracer.currentSpan().context().traceId();
      }
    } catch (Exception e) {
      log.debug("Could not extract trace ID", e);
    }
    return UUID.randomUUID().toString();
  }

  // Domain exceptions (4xx)

  @ExceptionHandler(InvalidProviderException.class)
  public ProblemDetail handleInvalidProvider(InvalidProviderException e) {
    return createProblemDetail(HttpStatus.BAD_REQUEST, "Invalid Provider", e.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFound(UserNotFoundException e) {
    return createProblemDetail(HttpStatus.NOT_FOUND, "User Not Found", e.getMessage());
  }

  @ExceptionHandler(UserIdentityAlreadyExistsException.class)
  public ProblemDetail handleUserIdentityAlreadyExists(UserIdentityAlreadyExistsException e) {
    return createProblemDetail(HttpStatus.CONFLICT, "Identity Already Exists", e.getMessage());
  }

  // Application exceptions (typically 5xx)

  @ExceptionHandler(UserCreationFailedException.class)
  public ProblemDetail handleUserCreationFailed(UserCreationFailedException e) {
    log.error("User creation failed", e);
    return createProblemDetail(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Creation Failed",
      "An error occurred while creating the user. Please try again."
    );
  }

  @ExceptionHandler(UserIdentityPersistenceException.class)
  public ProblemDetail handleUserIdentityPersistence(UserIdentityPersistenceException e) {
    log.error("User identity persistence error", e);
    return createProblemDetail(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "An Error Has Occurred",
      "An error occurred while saving. Please try again."
    );
  }

  // Helper

  private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String traceId) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setTitle(title);
    problemDetail.setProperty("traceId", traceId);
    return problemDetail;
  }
}
```

---

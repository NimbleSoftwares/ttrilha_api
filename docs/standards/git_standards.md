# Development Standards - ttrilha_api

## Branch Naming Convention

All branches should follow this pattern:

```
<type>/<scope>-<description>
```

### Type Prefixes

- `feature/` — New functionality
- `fix/` — Bug fixes
- `refactor/` — Code improvements (no behavior change)
- `docs/` — Documentation
- `chore/` — Maintenance, dependency updates
- `test/` — New tests or test improvements
- `performance/` — Performance improvements
- `security/` — Security fixes or improvements

### Scope

Domain or feature area of the change:
- `auth` — Authentication/authorization features
- `user` — User management
- `api` — API endpoints
- `db` — Database schema or queries
- `config` — Configuration changes
- `security` — Security-related changes
- `docs` — Documentation
- `ci` — CI/CD pipeline
- `deps` — Dependency updates

### Description

Keep concise (2-4 words, ~20 chars max), lowercase with hyphens:
- ✅ `feature/auth-jwt-login`
- ✅ `fix/db-migration-error`
- ✅ `chore/deps-update-spring`
- ❌ `feature/auth` (too vague)
- ❌ `FEATURE/AUTH-JWT-LOGIN` (wrong case)
- ❌ `feature/add_jwt_authentication_logic_for_users` (too long)

### Examples

```
feature/auth-jwt-login
fix/security-sql-injection
refactor/user-service-layer
chore/deps-spring-boot-35
docs/api-endpoints
test/auth-integration-tests
perf/query-optimization
sec/password-hashing-bcrypt
```

## Git Commit Messages

Follow Conventional Commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat:` — New feature
- `fix:` — Bug fix
- `refactor:` — Code refactoring
- `test:` — Test additions or changes
- `docs:` — Documentation
- `chore:` — Build, CI, dependency updates
- `perf:` — Performance improvements
- `sec:` — Security fixes
- `style:` — Code style (formatting, semicolons)

### Example Commits

```
feat(auth): add JWT authentication endpoint

Implements OAuth2 resource server configuration with JWT validation.
- Configure SecurityConfig for OAuth2 Resource Server
- Add JWT decoder bean with JWKS endpoint
- Add CORS configuration for API

Closes #123
```

```
fix(db): correct migration naming for PostgreSQL compatibility

Previous migration used invalid SQL syntax for PostgreSQL.

Fixes #456
```

```
chore(deps): update Spring Boot to 3.5.11
```

## Pull Request Guidelines

1. **Branch naming**: Use the convention above
2. **Title**: Prefix with type, follow pattern: `feat(scope): brief description`
3. **Description**: Explain "why" not "what"
4. **Testing**: Include test coverage for new code
5. **Review**: Wait for at least one approval before merging

## Code Style

- **Java**: Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **Formatting**: 4-space indentation
- **Naming**:
  - Classes: PascalCase
  - Methods/variables: camelCase
  - Constants: UPPER_SNAKE_CASE
  - Package names: lowercase.reverse.domain.notation
- **Architecture**: Layered structure (controller → service → repository)

## Technology Stack

- **Framework**: Spring Boot 3.5.11
- **Language**: Java 21
- **Database**: PostgreSQL
- **Auth**: OAuth2 Resource Server with JWT
- **API Docs**: SpringDoc OpenAPI (Swagger)
- **Migration**: Flyway
- **Build**: Maven

## Security Notes

- **Passwords**: Use bcrypt for hashing (Spring Security's PasswordEncoder)
- **Secrets**: Use environment variables or `.env` (never commit sensitive data)
- **SQL Injection**: Always use parameterized queries via JPA
- **CORS**: Configure appropriately in SecurityConfig
- **OAuth2**: Validate JWT tokens from trusted JWKS endpoint

## Before You Commit

- [ ] Code follows style guide
- [ ] Tests added/updated for new features
- [ ] No hardcoded secrets or credentials
- [ ] Meaningful commit message
- [ ] Branch name follows convention

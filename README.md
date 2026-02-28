# ttrilha_api

## Database

### Running database
- Go to /docker/postgres
- Run `docker-compose up`
- To verify it's running:
     docker-compose ps
     docker-compose logs postgres
- Access with psql -h localhost -U ${POSTGRES_USER} -d ${POSTGRES_DB}

## Creating new tables
- see @docs/standards/db_standards.md

## Packages Structure
- see @docs/standards/hexagonal_arch_standards.html

## Versioning
- see @docs/standards/git_standards.md

## Error Handling
- see @docs/standards/error_handling_standards.md

## Tests
- see @docs/standards/test_standards.md

## API Documentation
- Document every controller and endpoint using swagger
Example:
```
  @Operation(
      summary = "Sync or create user",
      description = "Creates a new user or updates authenticated user data based on the provided JWT. Extracts provider and provider user ID from JWT sub claim",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "User successfully created or synced",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = SaveUserResponse.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
      @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired JWT"),
      @ApiResponse(responseCode = "403", description = "Forbidden - JWT audience or issuer mismatch"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
```
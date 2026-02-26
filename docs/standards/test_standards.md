# TEST STANDARDS - How to implement tests

## Naming Conventions
- Use @DisplayName annotation to name the test
- For test classes:
  - For Unit Tests use `Test` sufix
  - For Integration Tests use `IT` sufix
  - For Slice Tests use `SliceTest` sufix
- For test methods:
  - Use `should` prefix, followed by what should happen
  - Use `when` sufix, followed by the when clause

## Unit Tests
- Use JUnit with Mockito
- Create the happy path test and the main edge cases test

## Integration Tests
- Use Spring Boot Test
- Use Testcontainers for database testing
- Just create Integration tests for the REALLY important stuff

## Slice Tests
- Use spring annotations like @WebMvcTest, @DataJpaTest
- Create the happy path test and the main edge cases test
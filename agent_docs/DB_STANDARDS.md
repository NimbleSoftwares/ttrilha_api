# Database Standards

## Table Creation Scripts

### Structure & Order

All migration files should follow this structure:

```sql
-- 1. CREATE TYPES (if needed)
CREATE TYPE enum_name AS ENUM ('VALUE1', 'VALUE2');

-- 2. CREATE TABLE
CREATE TABLE IF NOT EXISTS table_name (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    -- Other columns
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ  -- For soft deletes
);

-- 3. CONSTRAINTS: PRIMARY KEY
ALTER TABLE table_name ADD CONSTRAINT PK_TABLE_NAME PRIMARY KEY (id);

-- 4. CONSTRAINTS: FOREIGN KEYS
ALTER TABLE table_name ADD CONSTRAINT FK_TABLE_NAME_REFERENCED_TABLE
    FOREIGN KEY (foreign_key_column)
    REFERENCES referenced_table(id) ON DELETE CASCADE;

-- 5. INDEXES: Unique Indexes
CREATE UNIQUE INDEX IDX_TABLE_NAME_COLUMN_ACTIVE ON table_name (column)
    WHERE deleted_at IS NULL;

-- 6. INDEXES: Regular Indexes
CREATE INDEX IDX_TABLE_NAME_COLUMN ON table_name (column)
    WHERE deleted_at IS NULL;

-- 7. COMMENTS: Table
COMMENT ON TABLE table_name IS 'Description of what this table stores';

-- 8. COMMENTS: Columns
COMMENT ON COLUMN table_name.id IS 'Unique identifier';
COMMENT ON COLUMN table_name.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN table_name.updated_at IS 'Record last update timestamp';
COMMENT ON COLUMN table_name.deleted_at IS 'Soft delete timestamp, null if active';
```

---

## Key Guidelines

### Column Naming
- Use `snake_case` for all column names
- Use `id` for primary key (UUID)
- Use `created_at`, `updated_at`, `deleted_at` consistently across all tables
- Use `VARCHAR(254)` for email (RFC 5321 compliant)

### Primary Keys
- Always use UUID with `DEFAULT gen_random_uuid()`
- Define as `NOT NULL` in column definition
- Add `PK_` constraint explicitly with `ALTER TABLE`

### Timestamps
- `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()` — immutable after creation
- `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()` — updated on every change
- `deleted_at TIMESTAMPTZ NULL` — for soft deletes (optional)

### Soft Deletes
- Add `deleted_at TIMESTAMPTZ` column to tables that need audit trails
- Use **partial indexes** to exclude soft-deleted records:
  ```sql
  CREATE UNIQUE INDEX IDX_TABLE_NAME_COLUMN_ACTIVE ON table_name (column)
      WHERE deleted_at IS NULL;
  ```
- Queries must include `WHERE deleted_at IS NULL` to exclude deleted records

### Foreign Keys
- Name format: `FK_TABLE_NAME_REFERENCED_TABLE`
- Always include `ON DELETE CASCADE` for dependent records
- Add `NOT NULL` if the relationship is required

### Indexes
- **Unique indexes**: For columns that should be unique (email, username, etc.)
  - Always use partial index with soft deletes: `WHERE deleted_at IS NULL`
- **Regular indexes**: For frequently queried columns (user_id, status, etc.)
  - Use partial indexes with soft deletes where applicable
- Prefix: `IDX_TABLE_NAME_COLUMN`

### Enums
- Define before table creation
- Name format: `{entity}_{attribute}` (e.g., `oauth_provider`, `order_status`)
- Use `NOT NULL DEFAULT 'VALUE'` in table definition when appropriate

### Comments
- Always document tables and columns
- Be concise but descriptive
- Include business context (e.g., "Used for OAuth2")

---

## Example: User Tables

**See:** `V1__create_users_table.sql`, `V2__create_user_identities_table.sql`

### Users Table Pattern
- UUID primary key
- Email with partial unique index (allows reuse after soft delete)
- Soft delete support (deleted_at)
- Standard timestamps (created_at, updated_at)

### User Identities Table Pattern
- Compound primary key (user_id, provider, provider_user_id)
- Foreign key to users with ON DELETE CASCADE
- Provider as ENUM type (type safety)
- Partial index on active records

---

## Migration File Naming

Use Flyway convention:
```
V{number}__{description}.sql
```

Examples:
- `V1__create_users_table.sql`
- `V2__create_user_identities_table.sql`
- `V3__add_user_email_verification.sql`
- `V4__add_soft_delete_support.sql`

---

## Best Practices

✅ **DO:**
- Use `IF NOT EXISTS` for idempotency
- Define PKs explicitly with `ALTER TABLE`
- Add comments to all tables and columns
- Use partial indexes for soft-deleted records
- Name constraints consistently
- Use ENUM types for fixed value sets
- Test migrations in dev before production

❌ **DON'T:**
- Use serial integers as primary keys (use UUID instead)
- Create global UNIQUE constraints on soft-delete columns (use partial indexes)
- Skip foreign key constraints
- Forget to include `WHERE deleted_at IS NULL` in relevant indexes
- Use ambiguous column names
- Skip comments on tables and columns

---

## Queries with Soft Deletes

Always filter out deleted records:

```sql
-- ✅ Correct: Returns only active users
SELECT * FROM users WHERE deleted_at IS NULL;

-- ✅ Correct: Finds active user by email (uses partial index)
SELECT * FROM users WHERE email = 'user@example.com' AND deleted_at IS NULL;

-- ❌ Wrong: Returns deleted users too
SELECT * FROM users WHERE email = 'user@example.com';
```

For JPA/Hibernate, use `@Where` annotation:
```java
@Entity
@Where(clause = "deleted_at IS NULL")
public class User { ... }
```

---

## Constraint Naming Convention

| Type | Format | Example |
|------|--------|---------|
| Primary Key | `PK_{TABLE}` | `PK_USERS` |
| Foreign Key | `FK_{TABLE}_{REFERENCED}` | `FK_USER_IDENTITIES_USERS` |
| Unique Index | `IDX_{TABLE}_{COLUMN}_ACTIVE` | `IDX_USERS_EMAIL_ACTIVE` |
| Regular Index | `IDX_{TABLE}_{COLUMN}` | `IDX_USER_IDENTITIES_USER_ID` |
| Check Constraint | `CHK_{TABLE}_{CONDITION}` | `CHK_ORDERS_TOTAL_POSITIVE` |

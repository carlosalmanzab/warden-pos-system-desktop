# WARDEN-POS-SYS - Backend Core Context

## Tech Stack
- **Language:** Java 17+ / Spring Boot 3.x
- **Database:** SQLite (Desarrollo) / PostgreSQL (Producción)
- **Persistence Layer:** jOOQ (Strictly Type-Safe). **NO Hibernate/JPA**.
- **Database Migrations:** Flyway

## Core Architecture Rules
1. **No Hidden States:** All database queries must be explicitly written using jOOQ `DSLContext`.
2. **Transaction Integrity:** Any inventory or ledger mutations (Kardex, Stock, Sales) MUST be encapsulated inside a `@Transactional` service layer.
3. **Pessimistic Locking:** Critical calculations (like Weighted Average Cost in Kardex) must lock rows using `.forUpdate()` to prevent race conditions.
4. **Data Types:** Financial figures and stock quantities MUST use `BigDecimal`, never `double` or `float`.

## Generated Code Package
- jOOQ generated tables are under: `com.warden.possys.jooq.Tables.*`
- Always use static imports for tables (e.g., `import static com.warden.possys.jooq.Tables.PRODUCTS;`).
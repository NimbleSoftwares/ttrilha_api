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

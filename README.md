# ttrilha_api

# Database

## Running database
- Go to /docker/postgres
- Run `docker-compose up`
- To verify it's running:
     docker-compose ps
     docker-compose logs postgres
- Access with psql -h localhost -U ${POSTGRES_USER} -d ${POSTGRES_DB}

## Creating new tables
- see @agent-docs/db_standards.md
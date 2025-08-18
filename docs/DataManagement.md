## Data Management

### Updating schema, upgrading database and managing data:

Schema of database is application driven with help of Flyway versioning. 
Because application logic is interconnected with database it makes
sense to make changes to database only from there so there is no mismatch.
It is best to have database schema finalized from start but changes can happen over time.
Flyway allows to apply sequential changes-migrations of schema to existing or new database.
Migration scripts are located in `resources/db/migration`

Altering schema is problematic when table already contains records.
Then when for example adding new column with notnull constraint,
we have to set the column to some value for all existing records.
In case of mismatch between application driven schema versioning and database itself,
Flyway would throw fatal exception and the mismatch has to be fixed manually.
For dev-testing purposes this is overridden with `"spring.flyway.clean-on-validation-error=true"`

As application and database docker compose configs are separated,
data management is simplified.
We can stop or pause the application container with database running and perform 
backup of database before applying schema changes or upgrading database.

    docker exec -it usermanager-postgres pg_dumpall -U usermanager > dump.sql


ALTER TABLE users
ADD birth_date DATE;
ALTER TABLE users
ADD registered_date DATE;
UPDATE users SET registered_date=NOW();
ALTER TABLE users
ALTER COLUMN registered_date SET NOT NULL;


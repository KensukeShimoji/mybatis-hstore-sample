CREATE EXTENSION hstore;

DROP TABLE IF EXISTS client CASCADE;
CREATE TABLE client
(
    id VARCHAR(255) PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    custom_properties hstore
);
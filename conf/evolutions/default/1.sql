# User schema
 
# --- !Ups

CREATE TABLE user (
    email      text NOT NULL PRIMARY KEY,
    password   text NOT NULL
);

# --- !Downs

DROP TABLE user;


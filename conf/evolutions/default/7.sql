# Users schema
 
# --- !Ups

ALTER TABLE "Result"
   ALTER COLUMN value TYPE bigint;
 
# --- !Downs
 
ALTER TABLE "Result"
   ALTER COLUMN value TYPE integer;
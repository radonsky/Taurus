# Users schema
 
# --- !Ups

ALTER TABLE "Match"
   ALTER COLUMN create_time TYPE bigint;
 
# --- !Downs
 
ALTER TABLE "Match"
   ALTER COLUMN create_time TYPE integer;
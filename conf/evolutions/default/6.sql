# Users schema
 
# --- !Ups

ALTER TABLE "Group"
  ADD CONSTRAINT "Group_unique_name" UNIQUE (name);
 
# --- !Downs
 
ALTER TABLE "Group"
  DROP CONSTRAINT "Group_unique_name";
# Users schema
 
# --- !Ups

ALTER TABLE "Result" DROP CONSTRAINT "Result_match_id_fkey";

UPDATE "Match" SET match_id = match_id + 10000;
UPDATE "Result" SET match_id = match_id + 10000;

SELECT setval('"Match_match_id_seq"', (SELECT coalesce(MAX(match_id), 10000) FROM "Match"));

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_match_id_fkey" FOREIGN KEY (match_id)
      REFERENCES "Match" (match_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE "Result" SET result_id = result_id + 10000;
      
SELECT setval('"Result_result_id_seq"', (SELECT coalesce(MAX(result_id), 10000) FROM "Result"));

# --- !Downs
 
ALTER TABLE "Result" DROP CONSTRAINT "Result_match_id_fkey";

UPDATE "Match" SET match_id = match_id - 10000;
UPDATE "Result" SET match_id = match_id - 10000;

SELECT setval('"Match_match_id_seq"', (SELECT coalesce(MAX(match_id), 0) FROM "Match"));

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_match_id_fkey" FOREIGN KEY (match_id)
      REFERENCES "Match" (match_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE "Result" SET result_id = result_id - 10000;
      
SELECT setval('"Result_result_id_seq"', (SELECT coalesce(MAX(result_id), 0) FROM "Result"));

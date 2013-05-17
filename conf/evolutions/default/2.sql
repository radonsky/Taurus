# Users schema
 
# --- !Ups

ALTER TABLE "Match" DROP CONSTRAINT "Match_game_id_fkey";

UPDATE "Game" SET game_id = game_id + 1000;
UPDATE "Match" SET game_id = game_id + 1000;

SELECT setval('"Game_game_id_seq"', (SELECT coalesce(MAX(game_id), 1000) FROM "Game"));

ALTER TABLE "Match"
  ADD CONSTRAINT "Match_game_id_fkey" FOREIGN KEY (game_id)
      REFERENCES "Game" (game_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "Result" DROP CONSTRAINT "Result_player_id_fkey";
      
UPDATE "Player" SET player_id = player_id + 1000;
UPDATE "Result" SET player_id = player_id + 1000;

SELECT setval('"Player_player_id_seq"', (SELECT coalesce(MAX(player_id), 1000) FROM "Player"));

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_player_id_fkey" FOREIGN KEY (player_id)
      REFERENCES "Player" (player_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

# --- !Downs
 
ALTER TABLE "Match" DROP CONSTRAINT "Match_game_id_fkey";

UPDATE "Game" SET game_id = game_id - 1000;
UPDATE "Match" SET game_id = game_id - 1000;

SELECT setval('"Game_game_id_seq"', (SELECT coalesce(MAX(game_id), 0) FROM "Game"));

ALTER TABLE "Match"
  ADD CONSTRAINT "Match_game_id_fkey" FOREIGN KEY (game_id)
      REFERENCES "Game" (game_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "Result" DROP CONSTRAINT "Result_player_id_fkey";
      
UPDATE "Player" SET player_id = player_id - 1000;
UPDATE "Result" SET player_id = player_id - 1000;

SELECT setval('"Player_player_id_seq"', (SELECT coalesce(MAX(player_id), 0) FROM "Player"));

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_player_id_fkey" FOREIGN KEY (player_id)
      REFERENCES "Player" (player_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

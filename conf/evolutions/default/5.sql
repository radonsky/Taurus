# Users schema
 
# --- !Ups

ALTER TABLE "Result" DROP CONSTRAINT "Result_match_id_fkey";

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_match_id_fkey" FOREIGN KEY (match_id)
      REFERENCES "Match" (match_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
      
ALTER TABLE "Match" DROP CONSTRAINT "Match_game_id_fkey";

ALTER TABLE "Match"
  ADD CONSTRAINT "Match_game_id_fkey" FOREIGN KEY (game_id)
      REFERENCES "Game" (game_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "Game" DROP CONSTRAINT "Game_group_id_fkey";

ALTER TABLE "Game"
  ADD CONSTRAINT "Game_group_id_fkey" FOREIGN KEY (group_id)
      REFERENCES "Group" (group_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
      
ALTER TABLE "Player" DROP CONSTRAINT "Player_group_id_fkey";

ALTER TABLE "Player"
  ADD CONSTRAINT "Player_group_id_fkey" FOREIGN KEY (group_id)
      REFERENCES "Group" (group_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
 
# --- !Downs
 
ALTER TABLE "Result" DROP CONSTRAINT "Result_match_id_fkey";

ALTER TABLE "Result"
  ADD CONSTRAINT "Result_match_id_fkey" FOREIGN KEY (match_id)
      REFERENCES "Match" (match_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "Match" DROP CONSTRAINT "Match_game_id_fkey";

ALTER TABLE "Match"
  ADD CONSTRAINT "Match_game_id_fkey" FOREIGN KEY (game_id)
      REFERENCES "Game" (game_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "Game" DROP CONSTRAINT "Game_group_id_fkey";

ALTER TABLE "Game"
  ADD CONSTRAINT "Game_group_id_fkey" FOREIGN KEY (group_id)
      REFERENCES "Group" (group_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "Player" DROP CONSTRAINT "Player_group_id_fkey";

ALTER TABLE "Player"
  ADD CONSTRAINT "Player_group_id_fkey" FOREIGN KEY (group_id)
      REFERENCES "Group" (group_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
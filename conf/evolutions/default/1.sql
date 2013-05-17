# Users schema
 
# --- !Ups
 
CREATE TABLE "Group" (
	group_id SERIAL PRIMARY KEY, 
	name TEXT, 
	description TEXT, 
	visibility INTEGER, 
	password TEXT, 
	owner_id TEXT
);

CREATE TABLE "Player" (
	player_id SERIAL PRIMARY KEY, 
	name TEXT, 
	group_id INTEGER DEFAULT 0, 
	FOREIGN KEY(group_id) REFERENCES "Group"(group_id)
);

CREATE TABLE "Game" (
	game_id SERIAL PRIMARY KEY,
	name TEXT,
	type INTEGER,
	duration INTEGER DEFAULT 0,
	group_id INTEGER DEFAULT 0,
	FOREIGN KEY(group_id) REFERENCES "Group"(group_id)
);

CREATE TABLE "Match" (
	match_id SERIAL PRIMARY KEY, 
	create_time INTEGER, 
	game_id INTEGER, 
	FOREIGN KEY(game_id) REFERENCES "Game"(game_id)
);

CREATE TABLE "Result" (
	result_id SERIAL PRIMARY KEY, 
	value INTEGER, 
	points INTEGER, 
	match_id INTEGER, 
	player_id INTEGER, 
	FOREIGN KEY(match_id) REFERENCES "Match"(match_id), 
	FOREIGN KEY(player_id) REFERENCES "Player"(player_id) 
);


 
# --- !Downs
 
DROP TABLE "Result";
DROP TABLE "Match";
DROP TABLE "Game";
DROP TABLE "Player";
DROP TABLE "Group";
/*
 * Copyright 2013 Marek Radonsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.language.postfixOps


case class Result (
	resultId: Pk[Long],
	value: Option[Long],
	points: Option[Int],
	`match`: Match,
	playerId: Long,
	gameType: Option[Int],
	groupId: Option[Long]) extends ModelEntity {

  override val id = resultId
  
}


object Result {
  
  val simple = {
    get[Pk[Long]]("result_id") ~
    get[Option[Long]]("value") ~
    get[Option[Int]]("points") ~
    Match.simple ~
    get[Long]("player_id") ~ 
    get[Option[Int]]("type") ~
    get[Option[Long]]("group_id") map {
      case id~value~points~m~playerId~gameType~groupId =>
        Result(id, value, points, m, playerId, gameType, groupId)
    }
  }

  def getAllForGroup(groupId: Long): List[Result] = {
    DB.withConnection { implicit connection =>
      SQL("select r.result_id, r.value, r.points, r.player_id, " +
            "m.match_id, m.create_time, m.game_id, " +
            "g.type, g.group_id " +
          "from \"Result\" r, \"Match\" m, \"Game\" g " +
          "where " +
            "r.match_id = m.match_id and " +
            "m.game_id = g.game_id and " +
            "g.group_id = {groupId}").on(
                'groupId -> groupId
          ).as(Result.simple *)
    }
  }  
  
  def getAllForGame(gameId: Long): List[Result] = {
    DB.withConnection { implicit connection =>
      SQL("select r.result_id, r.value, r.points, r.player_id, " +
            "m.match_id, m.create_time, m.game_id, " +
            "g.type, g.group_id " +
          "from \"Result\" r, \"Match\" m, \"Game\" g " +
          "where " +
            "r.match_id = m.match_id and " +
            "m.game_id = {gameId}").on(
                'gameId -> gameId
          ).as(Result.simple *)
    }
  }
  
  def save(r: Result): Option[Result] = {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO \"Result\"(value, points, match_id, player_id) " +
          "VALUES ({value}, {points}, {matchId}, {playerId})").on(
        'value -> r.value,
        'points -> r.points,
        'matchId -> r.`match`.id.get,
        'playerId -> r.playerId
      ).executeInsert().map { id =>
      	r.copy(new Id(id))
      }
    }
  }
  
  def save(col: List[Result]): Option[List[Result]] = 
    col.foldLeft[Option[List[Result]]](Some(Nil)) { (l, r) =>
      l match {
        case Some(list) => save(r).map( _ :: list)
        case None => None
      }
    }

  def count(): Long = {
    DB.withConnection { implicit connection =>
      SQL("select count(*) from \"Result\"").as(scalar[Long].single)
    }
  }

  
  import models.Match
  
  implicit val reads: Reads[Result] = (
    (__ \ "id").readNullable[Long] and
    (__ \ "value").readNullable[Long] and
    (__ \ "points").readNullable[Int] and
    (__ \ "match").read[Match] and
    (__ \ "player" \ "id").read[Long] and
    (__ \ "match" \ "game" \ "type").readNullable[Int] and
    (__ \ "player" \ "group" \ "id").readNullable[Long]
  ) ((id, value, points, `match`, playerId, gameType, groupId) => 
    Result(id.map( Id(_) ).getOrElse( NotAssigned ), value, points, 
        `match`, playerId, gameType, groupId))

        
  implicit val writes: Writes[Result] = (
    (__ \ "id").writeNullable[Long] and
    (__ \ "value").writeNullable[Long] and
    (__ \ "points").writeNullable[Int] and
    (__ \ "match").write(
        (__ \ "id").writeNullable[Long] and
        (__ \ "createTime").writeNullable[Long] and
        (__ \ "game").write(
            (__ \ "id").write[Long] and
            (__ \ "type").writeNullable[Int] and
            (__ \ "group").write (
                (__ \ "id").writeNullable[Long]
            )
            tupled
        )
        tupled
    ) and 
    (__ \ "player").write(
        (__ \ "id").write[Long] and
        (__ \ "group").write(
            (__ \ "id").writeNullable[Long]
        )
        tupled
    )
    
  ) (
      (r: Result) => (r.resultId.toOption, r.value, r.points, 
         (r.`match`.matchId.toOption, r.`match`.createTime,
         (r.`match`.gameId, r.gameType, r.groupId)), (r.playerId,
         r.groupId))
    )

}

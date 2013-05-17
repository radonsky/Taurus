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
import play.api.libs.json.util._
import play.api.libs.functional.syntax._

case class Match (
    matchId: Pk[Long],
    createTime: Option[Long],
    gameId: Long) extends ModelEntity {
  
  override val id = matchId
   
}

object Match extends ModelObject[Match] {
  
  val simple = {
	get[Pk[Long]]("match_id") ~
	get[Option[Long]]("create_time") ~
	get[Long]("game_id") map {
	  case id~createTime~gameId =>
	    Match(id, createTime, gameId)
	}
  }
  
  def findAll(): List[Match] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Match\"").as(Match.simple *)
    }
  }
  
  def findById(id: Long): Option[Match] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Match\" where match_id = {id}")
      	.on('id -> id).as(Match.simple *).headOption
    }    
  }
  
  def findAllForGame(gameId: Long): List[Match] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Match\" where game_id = {id}")
      	.on('id -> gameId).as(Match.simple *)
    }    
  }
  
  def save(m: Match): Option[Match] = {
    DB.withConnection { implicit connection =>
      SQL("insert into \"Match\"(create_time, game_id) " +
      		"values ({createTime}, {gameId})").on(
        'createTime -> m.createTime,
        'gameId -> m.gameId
      ).executeInsert().map { id =>
      	m.copy(new Id(id))
      }
    }
  }
  
  def delete(id: Long): Int = {
    DB.withConnection { implicit connection =>
      SQL("delete from \"Match\" where match_id = {id}")
      	.on('id -> id).executeUpdate()
    }    
  }

  def update(id: Long, m: Match): Int = {
    DB.withConnection { implicit connection =>
      SQL("update \"Match\" set create_time = {createTime}, game_id = {gameId} " +
      		"where match_id = {id}").on(
      	'id -> id,	    
        'createTime -> m.createTime,
        'gameId -> m.gameId
      ).executeUpdate()
    }
  }
  
  def count(): Long = {
    DB.withConnection { implicit connection =>
      SQL("select count(*) from \"Match\"").as(scalar[Long].single)
    }
  }

  
  implicit val format: Format[Match] = (
    (__ \ "id").formatNullable[Long] and
    (__ \ "createTime").formatNullable[Long] and
    (__ \ "game" \ "id").format[Long]
  ) ((id, createTime, gameId) => Match(id.map( Id(_) ).getOrElse( NotAssigned ), createTime, gameId), 
    (m: Match) => (m.matchId.toOption, m.createTime, m.gameId))
    
}

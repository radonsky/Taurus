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

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.language.postfixOps

case class Game(
    gameId: Pk[Long],
    name: Option[String],
    gameType: Option[Int],
    duration: Long,
    groupId: Long) extends ModelEntity {
  
  override val id = gameId
    
}

object Game extends GroupDependentObject[Game] {
  
  val simple = {
    get[Pk[Long]]("game_id") ~
    get[Option[String]]("name") ~
    get[Option[Int]]("type") ~
    get[Long]("duration") ~
    get[Long]("group_id") map {
      case id~name~gameType~duration~groupId =>
        Game(id, name, gameType, duration, groupId)
    }
  }
  
  def findAll(): List[Game] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Game\"").as(Game.simple *)
    }
  }
  
  def findById(id: Long): Option[Game] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Game\" where game_id = {id}")
      	.on('id -> id).as(Game.simple *).headOption
    }    
  }
  
  def save(game: Game): Option[Game] = {
    DB.withConnection { implicit connection =>
      SQL("insert into \"Game\"(name, type, duration, group_id) " +
      		"values ({name}, {type}, {duration}, {group_id})").on(
        'name -> game.name,
        'type -> game.gameType,
        'duration -> game.duration,
        'group_id -> game.groupId
      ).executeInsert().map { id =>
      	game.copy(new Id(id))
      }
    }
  }
 
  def delete(id: Long): Int = {
    DB.withConnection { implicit connection =>
      SQL("delete from \"Game\" where game_id = {id}")
      	.on('id -> id).executeUpdate()
    }    
  }
  
  def update(id: Long, game: Game): Int = {
    DB.withConnection { implicit connection =>
      SQL("update \"Game\" set name = {name}, type = {type}, " +
      		"duration = {duration}, group_id = {group_id} " +
      		"where game_id = {id}").on(
      	'id -> id,	    
        'name -> game.name,
        'type -> game.gameType,
        'duration -> game.duration,
        'group_id -> game.groupId
      ).executeUpdate()
    }
  }
  
  def findAllWithGroup(groupId: Long) = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Game\" where group_id = {id}")
      	.on('id -> groupId).as(Game.simple *)
    }    
  }
  
  def deleteResults(id: Long): Int = {
    DB.withConnection { implicit connection =>
      SQL("delete from \"Match\" where game_id = {id}")
        .on('id -> id).executeUpdate()
    }    
  }

  def count(): Long = {
    DB.withConnection { implicit connection =>
      SQL("select count(*) from \"Game\"").as(scalar[Long].single)
    }
  }

  implicit val format: Format[Game] = (
      (__ \ "id").formatNullable[Long] and
      (__ \ "name").formatNullable[String] and
      (__ \ "type").formatNullable[Int] and
      (__ \ "duration").format[Long] and
      (__ \ "group" \ "id").format[Long]
    ) ((id, name, gameType, duration, group) =>
    Game(id.map( Id(_) ).getOrElse( NotAssigned ), name, gameType, duration, group),
    (g: Game) => (g.gameId.toOption, g.name, g.gameType, g.duration, g.groupId))

}


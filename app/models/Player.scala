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



case class Player(
    playerId: Pk[Long],
    name: Option[String],
    groupId: Long) extends ModelEntity {
  
  override val id = playerId
  
}
    
object Player extends GroupDependentObject[Player] {
  
  val simple = {
	get[Pk[Long]]("player_id") ~
	get[Option[String]]("name") ~
	get[Long]("group_id") map {
	  case id~name~groupId =>
	    Player(id, name, groupId)
	}
  }
  
  def findAll(): List[Player] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Player\"").as(Player.simple *)
    }
  }
  
  def findById(id: Long): Option[Player] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Player\" where player_id = {id}")
      	.on('id -> id).as(Player.simple *).headOption
    }    
  }
  
  def save(player: Player): Option[Player] = {
    DB.withConnection { implicit connection =>
      SQL("insert into \"Player\"(name, group_id) " +
      		"values ({name}, {groupId})").on(
        'name -> player.name,
        'groupId -> player.groupId
      ).executeInsert().map { id =>
      	player.copy(new Id(id))
      }
    }
  }
  
  def delete(id: Long): Int = {
    DB.withConnection { implicit connection =>
      SQL("delete from \"Player\" where player_id = {id}")
      	.on('id -> id).executeUpdate()
    }    
  }

  def update(id: Long, player: Player): Int = {
    DB.withConnection { implicit connection =>
      SQL("update \"Player\" set name = {name}, group_id = {groupId} " +
      		"where player_id = {id}").on(
      	'id -> id,	    
        'name -> player.name,
        'groupId -> player.groupId
      ).executeUpdate()
    }
  }
  
  def findAllWithGroup(groupId: Long) = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Player\" where group_id = {id}")
      	.on('id -> groupId).as(Player.simple *)
    }    
  }
  
  def count(): Long = {
    DB.withConnection { implicit connection =>
      SQL("select count(*) from \"Player\"").as(scalar[Long].single)
    }
  }

  
  
  implicit val format: Format[Player] = (
    (__ \ "id").formatNullable[Long] and
    (__ \ "name").formatNullable[String] and
    (__ \ "group" \ "id").format[Long]
  ) (
      (id, name, group) => Player(id.map( Id(_) ).getOrElse( NotAssigned ), name, group), 
        (p: Player) => (p.playerId.toOption, p.name, p.groupId)
    )
 
  
}

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
import ModelUtils._


case class Group (
    groupId: Pk[Long], 
    name: Option[String], 
    description: Option[String], 
    visibility: Option[Int],
    password: Option[String],
    ownerId: Option[String]) extends ModelEntity {
  
  override val id = groupId
    
}

    
object Group extends ModelObject[Group] {
        
  val simple = {
    get[Pk[Long]]("group_id") ~
    get[Option[String]]("name") ~
    get[Option[String]]("description") ~
    get[Option[Int]]("visibility") ~
    get[Option[String]]("password") ~
    get[Option[String]]("owner_id") map {
      case id~name~description~visibility~password~ownerId =>
        Group(id, name, description, visibility, password, ownerId)
    }
  }
  
  
  def findAll(): List[Group] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Group\"").as(Group.simple *)
    }
  }
  
  def findById(id: Long): Option[Group] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Group\" where group_id = {id}")
      	.on('id -> id).as(Group.simple *).headOption
    }    
  }

  def findByName(name: String): List[Group] = {
    DB.withConnection { implicit connection =>
      SQL("select * from \"Group\" where name ilike {name}")
        .on('name -> escapeForLike(name)).as(Group.simple *)
    }    
  }
  
  def findByIds(ids: Array[Int]): List[Group] = {
    val range = 0 until ids.length
    val params = range.map("id" + _)
    val paramValues = ids.map(toParameterValue(_))
    DB.withConnection { implicit connection =>
      SQL("select * from \"Group\" where group_id in ({%s})"
          .format(params.mkString("},{")))
      .on(params.zip(paramValues):_*)
      .as(Group.simple *)
    }
  }
  
  def save(group: Group): Option[Group] = {
    DB.withConnection { implicit connection =>
      SQL("insert into \"Group\"(name, description, visibility, password, owner_id) " +
      		"values ({name}, {description}, {visibility}, {password}, {owner_id})").on(
        'name -> group.name,
        'description -> group.description,
        'visibility -> group.visibility,
        'password -> group.password,
        'owner_id -> group.ownerId
      ).executeInsert().map { id =>
      	group.copy(Id(id))
      }
    }
  }
 
  def delete(id: Long): Int = {
    DB.withConnection { implicit connection =>
      SQL("delete from \"Group\" where group_id = {id}")
      	.on('id -> id).executeUpdate()
    }    
  }
  
  def update(id: Long, group: Group): Int = {
    DB.withConnection { implicit connection =>
      SQL("update \"Group\" set name = {name}, description = {description}, " +
      		"visibility = {visibility}, password = {password}, owner_id = {owner_id} " +
      		"where group_id = {id}").on(
      	'id -> id,	    
        'name -> group.name,
        'description -> group.description,
        'visibility -> group.visibility,
        'password -> group.password,
        'owner_id -> group.ownerId
      ).executeUpdate()
    }
  }

  def count(): Long = {
    DB.withConnection { implicit connection =>
      SQL("select count(*) from \"Group\"").as(scalar[Long].single)
    }
  }
  
  
  implicit val format: Format[Group] = (
      (__ \ "id").formatNullable[Long] and
      (__ \ "name").formatNullable[String] and
      (__ \ "description").formatNullable[String] and
      (__ \ "visibility").formatNullable[Int] and
      (__ \ "password").formatNullable[String] and
      (__ \ "ownerId").formatNullable[String]
  ) (
      (id, name, description, visibility, password, ownerId) =>
        Group(id.map( Id(_) ).getOrElse( NotAssigned ), 
            name, description, visibility, password, ownerId),
        (g: Group) => (g.groupId.toOption, g.name, g.description, 
            g.visibility, g.password, g.ownerId)
    )
  
}


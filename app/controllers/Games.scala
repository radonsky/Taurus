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
package controllers

import models._
import play.api.mvc.RequestHeader
import play.api.mvc.Action
import play.api.libs.json.Writes
import play.api.libs.json.Reads
import anorm.NotAssigned
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.JsUndefined
import play.api.libs.json.JsNull
import play.api.mvc.SimpleResult
import play.api.http.ContentTypes


object Games extends GroupDependentController[Game] {

  override val objectName = "Game"
  override val modelObject = Game
  override val objectFormat = Game.format
  
  override def getObjectURI(id: Long)(implicit request: RequestHeader) = controllers.routes.Games.get(id).absoluteURL()
   
  def addResults(gameId: Long) = isAuthenticated(parse.json) { username => implicit request =>
  	implicit val reads = Reads.list(Result.reads)
  	implicit val writes = Writes.list(Result.writes)
  	val results = request.body.as[List[Result]]
    Match.save(Match(NotAssigned, Some(System.currentTimeMillis()), gameId)).map { m =>
      Game.findById(m.gameId).map { g =>
        Group.findById(g.groupId).map { gr =>
          val objs = results.map { r => 
            Result(r.id, r.value, r.points, m, r.playerId, g.gameType, gr.id.toOption) 
          }
          Result.save(objs).map { saved =>
          	Created(Json.toJson(saved))
          }.getOrElse( BadRequest(Error("Couldn't save the results").toJson) )
        }.getOrElse( NotFound(Error("Group not found").toJson) )
      }.getOrElse( NotFound(Error("Game not found").toJson) )
    }.getOrElse( BadRequest(Error("Couldn't save the result").toJson) )
  }
  
  def getResults(id: Long) = isAuthenticated { username => implicit request =>
    implicit val writes = Writes.list(Result.writes)
    val results = Result.getAllForGame(id)
    Ok(Json.toJson(results))
  }
  
  def deleteResults(id: Long) = isAuthenticated { username => implicit request =>
    Game.deleteResults(id)
    Ok.as(ContentTypes.JSON)
  }
  
}
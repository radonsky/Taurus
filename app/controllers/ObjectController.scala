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

import play.api._
import play.api.mvc._
import models.ModelObject
import play.api.libs.json.Writes
import play.api.libs.json.Format
import play.api.libs.json.Json
import models.ModelEntity
import models.Error
import org.postgresql.util.PSQLException
import play.api.http.ContentTypes

trait ObjectController[T <: ModelEntity] extends Controller with Security {
  
  val objectName: String
  val modelObject: ModelObject[T]
  val objectFormat: Format[T]
  def getObjectURI(id: Long)(implicit request: RequestHeader): String
  
  def list() = isAuthenticated { username => implicit request =>
    val objs = modelObject.findAll()
    implicit val format = Writes.list(objectFormat)
    Ok(Json.toJson(objs))
  }
  
  
  def add() = isAuthenticated(parse.json) { username => implicit request =>
    implicit val format = objectFormat
    try {
      modelObject.save(request.body.as[T]).map { obj => 
        val uri = getObjectURI(obj.id.get) 
        Created(Json.toJson(obj)).withHeaders(
            "Location" -> uri,
            "Content-Location" -> uri
        )
      }.getOrElse {
        BadRequest(Error(objectName + " wasn't saved to the database").toJson)
      }
    } catch {
      case e: PSQLException => 
        if (e.getServerErrorMessage().getMessage().contains(
            "duplicate key value violates unique constraint")) {
          Conflict(Error("Couldn't add duplicate " + objectName).toJson)
        } else {
          throw e
        }
    }
  }

  def get(id: Long) = isAuthenticated { username => implicit request =>
    modelObject.findById(id).map { obj =>
      implicit val format = objectFormat
      Ok(Json.toJson(obj))
    }.getOrElse {
      NotFound(Error(objectName + " with id " + id + " wasn't found").toJson)
    }
  }
  
  def delete(id: Long) = isAuthenticated { username => implicit request =>
    modelObject.delete(id)
    Ok.as(ContentTypes.JSON)
  }
  
  def update(id: Long) = isAuthenticated(parse.json) { username => implicit request =>
    implicit val format = objectFormat
    modelObject.update(id, request.body.as[T])
    Ok.as(ContentTypes.JSON)
  }
  
}
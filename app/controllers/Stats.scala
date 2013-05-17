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
import play.api.libs.json.Json
import models._

object Stats extends Controller with Security {

  def getTables = isAuthenticated { username => implicit request =>
    Ok(Json.obj(
        "groups" -> Group.count(),
        "players" -> Player.count(),
        "games" -> Game.count(),
        "matches" -> Match.count(),
        "results" -> Result.count()
    ))
  }
  
}
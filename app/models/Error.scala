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

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._


case class Error (
    message: String,
    stackTrace: Option[String]) {
  
  
  def toJson: JsValue = Json.toJson(this)
  
}

object Error {

  def apply(msg: String): Error = Error(msg, None)
  
  def apply(e: Throwable): Error = Error(e.getMessage(), Some(e.getStackTraceString))
    
  implicit val writes: Writes[Error] = (
    (__ \ "message").write[String] and
    (__ \ "stackTrace").writeNullable[String] 
  ) (unlift(Error.unapply))

}


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

import play.api.mvc.Results._
import play.api.mvc._
import org.apache.commons.codec.binary.Base64
import java.nio.charset.Charset

trait Security {
  
  import play.api.mvc.Security._
  
  private val charset = Charset.forName("UTF-8") 
  
  def username(request: RequestHeader) = {
    val authHeader = request.headers.get("Authorization")
    authHeader match {
      case Some(auth) => {
        val encoded = auth.replaceFirst("Basic ", "");
        val usrPassStr = new String(Base64.decodeBase64(encoded.getBytes(charset)), charset)
        val usrPass = usrPassStr.split(":")
        val userid = usrPass(0)
        val password = usrPass(1)
        if (password == Security.Password) {
          Some(userid)
        } else {
          None
        }
      }
      case None => None
    }
  }
  
  def onUnauthorized(request: RequestHeader) = 
    Unauthorized(views.html.defaultpages.unauthorized()).
      withHeaders("WWW-Authenticate" -> "Basic realm=\"Please log in to Taurus.\"")

  def isAuthenticated[A](p: BodyParser[A])(f: => String => Request[A] => Result) = {
    Authenticated(username, onUnauthorized) { user =>
      Action(p)(request => f(user)(request))
    }
  }
  
  def isAuthenticated(f: => String => Request[AnyContent] => Result): EssentialAction = 
    isAuthenticated(BodyParsers.parse.anyContent)(f)

}

object Security {
  val Password: String = System.getenv("TAURUS_HTTP_BASIC_PASSWORD")
}
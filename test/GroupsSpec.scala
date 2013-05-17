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
import org.specs2.mutable.Specification

import play.api.test._
import play.api.test.Helpers._

class GroupsSpec extends Specification {
  
	"respond to the /groups Action" in {
	  running(FakeApplication()) {
  
		  val Some(result) = routeAndCall(FakeRequest(GET, "/groups"))
		  
		  status(result) must equalTo(OK)
		  contentType(result) must beSome("application/json")
		  charset(result) must beSome("utf-8")
		  contentAsString(result) must contain("Hello Bob")
	  }
	}  

}
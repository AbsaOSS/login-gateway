/*
 * Copyright 2023 ABSA Group Limited
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

package za.co.absa.logingw.rest.service

import io.jsonwebtoken.{Claims, Jws, Jwts}
import org.scalatest.flatspec.AnyFlatSpec
import za.co.absa.logingw.model.User

import java.util
import scala.util.Try

class JWTServiceTest extends AnyFlatSpec {

  private val jwtService: JWTService = new JWTService()

  private val userWithoutGroups: User = User(
    name = "testUser",
    email = "test@gmail.com",
    groups = Seq.empty
  )

  private val userWithGroups: User = userWithoutGroups.copy(
    groups = Seq("testGroup1", "testGroup2")
  )

  private def parseJWT(jwt: String): Try[Jws[Claims]] = Try {
    Jwts.parserBuilder().setSigningKey(jwtService.publicKey).build().parseClaimsJws(jwt)
  }

  behavior of "generateToken"

  it should "return a JWT that is verifiable by `publicKey`" in {
    val jwt = jwtService.generateToken(userWithoutGroups)
    val parsedJWT = parseJWT(jwt)

    assert(parsedJWT.isSuccess)
  }

  it should "return a JWT with subject equal to User.name" in {
    val jwt = jwtService.generateToken(userWithoutGroups)
    val parsedJWT = parseJWT(jwt)
    val actualSubject = parsedJWT
      .map(_.getBody.getSubject)
      .get

    assert(actualSubject === userWithoutGroups.name)
  }

  it should "return a JWT with email claim equal to User.email" in {
    val jwt = jwtService.generateToken(userWithoutGroups)
    val parsedJWT = parseJWT(jwt)
    val actualSubject = parsedJWT
      .map(_.getBody.get("email", classOf[String]))
      .get

    assert(actualSubject === userWithoutGroups.email)
  }

  it should "turn groups into empty `groups` claim for user without groups" in {
    import scala.collection.JavaConverters._

    val jwt = jwtService.generateToken(userWithoutGroups)
    val parsedJWT = parseJWT(jwt)
    val actualGroups = parsedJWT
      .map(_.getBody.get("groups", classOf[util.ArrayList[String]]))
      .get
      .asScala

    assert(actualGroups === userWithoutGroups.groups)
  }

  it should "turn groups into non-empty `groups` claim for user with groups" in {
    import scala.collection.JavaConverters._

    val jwt = jwtService.generateToken(userWithGroups)
    val parsedJWT = parseJWT(jwt)
    val actualGroups = parsedJWT
      .map(_.getBody.get("groups", classOf[util.ArrayList[String]]))
      .get
      .asScala

    assert(actualGroups === userWithGroups.groups)
  }

}

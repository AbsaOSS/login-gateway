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

package za.co.absa.logingw.rest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.{ApiResponse, ApiResponses}
import io.swagger.v3.oas.annotations.tags.{Tag, Tags}

import java.util.concurrent.CompletableFuture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType}
import org.springframework.web.bind.annotation._
import za.co.absa.logingw.rest.service.TestService

import scala.concurrent.Future

/**
 * This is an initial endpoint to prove the setup works. Intended to be removed/changed.
 */
@RestController
@RequestMapping(Array("/api/test"))
class TestController @Autowired()(testService: TestService) {

  import za.co.absa.logingw.utils.implicits._

  @Tags(Array(new Tag(name = "test")))
  @Operation(
    summary = "Gives test response from TestService",
    description =
      """
       This only serves as a demonstration of successfully setup SpringBoot for RESTful accesss.
      """)
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "The controller is called normally and testing output is returned")
  ))
  @GetMapping(path = Array(""), produces = Array(MediaType.TEXT_PLAIN_VALUE))
  @ResponseStatus(HttpStatus.OK)
  def getTestResult(): CompletableFuture[String] = {
    Future.successful(s"Test controller: ${testService.getTestMessage}")
  }

}

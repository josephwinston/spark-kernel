/*
 * Copyright 2014 IBM Corp.
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

package com.ibm.spark.kernel.protocol.v5.content

import org.scalatest.{FunSpec, Matchers}
import play.api.data.validation.ValidationError
import play.api.libs.json._

class ShutdownRequestSpec extends FunSpec with Matchers {
  val shutdownRequestJson: JsValue = Json.parse("""
  {
    "restart": true
  }
  """)

  val shutdownRequest: ShutdownRequest = ShutdownRequest(
    true
  )

  describe("ShutdownRequest") {
    describe("implicit conversions") {
      it("should implicitly convert from valid json to a ShutdownRequest instance") {
        // This is the least safe way to convert as an error is thrown if it fails
        shutdownRequestJson.as[ShutdownRequest] should be (shutdownRequest)
      }

      it("should also work with asOpt") {
        // This is safer, but we lose the error information as it returns
        // None if the conversion fails
        val newShutdownRequest = shutdownRequestJson.asOpt[ShutdownRequest]

        newShutdownRequest.get should be (shutdownRequest)
      }

      it("should also work with validate") {
        // This is the safest as it collects all error information (not just first error) and reports it
        val ShutdownRequestResults = shutdownRequestJson.validate[ShutdownRequest]

        ShutdownRequestResults.fold(
          (invalid: Seq[(JsPath, Seq[ValidationError])]) => println("Failed!"),
          (valid: ShutdownRequest) => valid
        ) should be (shutdownRequest)
      }

      it("should implicitly convert from a ShutdownRequest instance to valid json") {
        Json.toJson(shutdownRequest) should be (shutdownRequestJson)
      }
    }
  }
}


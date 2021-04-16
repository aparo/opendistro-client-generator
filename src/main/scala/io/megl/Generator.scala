/*
 * Copyright 2021 Alberto Paro
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

package io.megl

import better.files._
import io.circe.syntax._
import io.megl.generators.OpenAPIGenerator
import io.megl.models.Root

object Generator extends App {

  val schemaJson=File.currentWorkingDirectory / "output" / "schema" / "schema.json"

  val schema=for {
    json <- io.circe.parser.parse(schemaJson.contentAsString)
    schema <- json.as[Root]
  } yield schema

//  print(schema)
  val gen=OpenAPIGenerator(schema.right.get)
  gen.generator()

}
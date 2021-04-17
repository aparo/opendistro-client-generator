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

package io.megl.models.klass

import better.files._
import io.circe.derivation.annotations.JsonCodec

@JsonCodec
final case class ClassDef(
  name: String,
  className: Option[String] = None,
  documentation: String = "",
  parent: Option[String] = None,
  var members: List[Member],
  fieldMode: Option[Boolean] = Some(false)
) {

  def reorderMembers(): Unit =
    members = members.filter(_.required) ::: members.filterNot(_.required).filterNot(_.name.startsWith("_")) ::: members
      .filterNot(_.required)
      .filter(_.name.startsWith("_"))

}

object ClassDef {
  def load(filename: File): Either[io.circe.Error, ClassDef] = {
    val data = filename.contentAsString
    for {
      json <- io.circe.parser.parse(data)
      obj  <- json.as[ClassDef]
    } yield {
      obj.reorderMembers()
      obj
    }
  }
}

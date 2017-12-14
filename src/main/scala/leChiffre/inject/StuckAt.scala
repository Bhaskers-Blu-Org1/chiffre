// Copyright 2017 IBM
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package leChiffre.inject

import chisel3._
import chisel3.util._
import leChiffre.scan._

class StuckAt(n: Int, id: String) extends Injector(n, id) {
  val mask = Reg(UInt(n.W))
  val value = Reg(UInt(n.W))

  lazy val info = StuckAtInjectorInfo(n)

  io.out := enabled & (
    (!mask & io.in) | ((mask & io.in) & value) | ((mask & !io.in) | value) )

  when (io.scan.clk) {
    enabled := false.B
    mask := io.scan.in ## (mask >> 1)
    value := mask(0) ## (value >> 1)
  }

  io.scan.out := value(0)
}

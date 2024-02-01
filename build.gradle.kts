/*
 * Copyright (C) 2024 Rick Busarow
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.rickbusarow.kgx.withBuildInitPlugin
import com.rickbusarow.lattice.core.VERSION_NAME

plugins {
  alias(libs.plugins.poko) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.rickBusarow.ktlint) apply false
  alias(libs.plugins.rickBusarow.doks)
  alias(libs.plugins.rickBusarow.moduleCheck)
  id("com.rickbusarow.lattice.jvm-module") apply false
  id("com.rickbusarow.lattice.root")
}

moduleCheck {
  deleteUnused = true
  checks.sortDependencies = true
}

lattice {

  composite {
  }
}

allprojects ap@{

  version = VERSION_NAME

  this@ap.plugins.withBuildInitPlugin {
    apply(plugin = libs.plugins.rickBusarow.ktlint.get().pluginId)

    dependencies {
      "ktlint"(rootProject.libs.rickBusarow.ktrules)
    }
  }
}

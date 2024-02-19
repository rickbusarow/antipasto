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

package com.rickbusarow.mahout.conventions

import com.diffplug.gradle.spotless.SpotlessApply
import com.rickbusarow.kgx.EagerGradleApi
import com.rickbusarow.kgx.applyOnce
import com.rickbusarow.kgx.matchingName
import com.rickbusarow.ktlint.KtLintFormatTask
import com.rickbusarow.mahout.api.DefaultMahoutTask
import com.rickbusarow.mahout.api.MahoutFixTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.language.base.plugins.LifecycleBasePlugin

@Suppress("UndocumentedPublicClass")
public abstract class CheckPlugin : Plugin<Project> {

  @OptIn(EagerGradleApi::class)
  override fun apply(target: Project) {

    target.plugins.applyOnce("base")

    val fix = target.tasks.register("fix", DefaultMahoutTask::class.java) { task ->

      task.group = "Verification"
      task.description = "Runs all auto-fix linting tasks"

      task.dependsOn(target.rootProject.tasks.withType(MahoutFixTask::class.java))
      task.dependsOn(target.rootProject.tasks.withType(SpotlessApply::class.java))
      task.dependsOn(target.tasks.matchingName("apiDump"))
      task.dependsOn(target.tasks.matchingName("deleteEmptyDirs"))
      task.dependsOn(target.tasks.matchingName("dependencyGuardBaseline"))
      task.dependsOn(target.tasks.matchingName("moduleCheckAuto"))
      task.dependsOn(target.tasks.withType(KtLintFormatTask::class.java))
      task.dependsOn(target.tasks.withType(MahoutFixTask::class.java))
    }

    // This is a convenience task which applies all available fixes before running `check`. Each
    // of the fixable linters use `mustRunAfter` to ensure that their auto-fix task runs before their
    // check-only task.
    target.tasks.register("checkFix", DefaultMahoutTask::class.java) { task ->

      task.group = "Verification"
      task.description = "Runs all auto-fix linting tasks, then runs all of the normal :check task"

      task.dependsOn(target.tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME))
      task.dependsOn(fix)
    }
  }
}

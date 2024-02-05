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

package com.rickbusarow.lattice.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSNode
import com.rickbusarow.lattice.generator.utils.Names
import com.rickbusarow.lattice.generator.utils.addAnnotation
import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Generated

/** */
public abstract class LatticeSymbolProcessor(
  private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

  protected val codeGenerator: CodeGenerator
    by lazy(LazyThreadSafetyMode.NONE) { environment.codeGenerator }
  protected val logger: KSPLogger by lazy(LazyThreadSafetyMode.NONE) { environment.logger }
  internal val names: Names by lazy(LazyThreadSafetyMode.NONE) { Names() }

  protected fun FileSpec.Builder.addGeneratedBy(): FileSpec.Builder = apply {
    addFileComment("Generated by %L", this@LatticeSymbolProcessor::class.qualifiedName!!)

    addAnnotation(Generated::class) {
      addMember("value = [%S]", this@LatticeSymbolProcessor::class.qualifiedName!!)
    }
  }

  protected inline fun KSNode.check(predicate: Boolean, lazyMessage: () -> String) {

    if (!predicate) {
      logger.error(lazyMessage(), this)
    }
  }
}
package com.squareup.anvil.sample

import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.classesAndInnerClass
import com.squareup.anvil.compiler.internal.hasAnnotation
import com.squareup.anvil.compiler.internal.safePackageString
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import java.io.File

@ExperimentalAnvilApi
@AutoService(CodeGenerator::class)
class SampleCodeGenerator : CodeGenerator {
    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>
    ): Collection<GeneratedFile> {
        return projectFiles
            .classesAndInnerClass(module)
            .filter { it.hasAnnotation(FqName("base-di.AutoInitialized"), module) }
            .map { clazz ->

                val generatedPackage = "generated_test" + clazz.containingKtFile.packageFqName
                    .safePackageString(dotPrefix = true, dotSuffix = false)

                @Language("kotlin")
                val generatedClass = """
                  package $generatedPackage

                  class GeneratedClass
                """.trimIndent()

                createGeneratedFile(
                    codeGenDir = codeGenDir,
                    packageName = generatedPackage,
                    fileName = "GeneratedClass",
                    content = generatedClass
                )
            }
            .toList()
    }
}
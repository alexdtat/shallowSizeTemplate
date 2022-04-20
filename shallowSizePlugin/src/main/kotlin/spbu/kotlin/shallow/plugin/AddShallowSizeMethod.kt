package spbu.kotlin.shallow.plugin

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties

const val FUNCTION_NAME = "shallowSize"

fun IrSimpleFunction.isShallowSizeFunction(): Boolean {
    return this.name.toString() == FUNCTION_NAME && this.valueParameters.isEmpty()
}

val Meta.GenerateShallowSize: CliPlugin
    get() = "Generate shallowSize method" {
        meta(
            classDeclaration(this, { element.isData() }) { declaration ->
                Transform.replace(
                    replacing = declaration.element,
                    newDeclaration = """
                            |$`@annotations` $visibility $kind $name $`(typeParameters)` $`(params)` $superTypes {
                            |   $body
                            |   fun $FUNCTION_NAME(): Int {
                            |       throw NotImplementedError("shallowSize function should be implemented")
                            |   }
                            | } """.trimIndent().`class`
                )
            },
            irClass { clazz ->
                if (clazz.isData) {
                    val shallowSize = clazz.functions.find { it.isShallowSizeFunction() }
                        ?: throw NoSuchMethodException("$FUNCTION_NAME not found")

                    val fieldsSummarizedSize = clazz.properties.map { it.backingField?.type?.byteSize() ?: 0 }.sum()
                    shallowSize.body =
                        DeclarationIrBuilder(pluginContext, shallowSize.symbol).irBlockBody {
                            +irReturn(irInt(fieldsSummarizedSize))
                        }
                }
                clazz
            }
        )
    }

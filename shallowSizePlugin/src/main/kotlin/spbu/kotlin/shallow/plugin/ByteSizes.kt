package spbu.kotlin.shallow.plugin

import org.jetbrains.kotlin.ir.types.*

object ByteSizesOfTypes {
    const val BOOLEAN = 1
    const val UNIT = 8
    const val DEFAULT = 8
}

fun IrType.byteSize() = when {
    isChar() -> Char.SIZE_BYTES
    isByte() -> Byte.SIZE_BYTES
    isUByte() -> UByte.SIZE_BYTES
    isShort() -> Short.SIZE_BYTES
    isUShort() -> UShort.SIZE_BYTES
    isInt() -> Int.SIZE_BYTES
    isLong() || isULong() -> Long.SIZE_BYTES
    isFloat() -> Float.SIZE_BYTES
    isDouble() -> Double.SIZE_BYTES
    isBoolean() -> ByteSizesOfTypes.BOOLEAN
    isUnit() -> ByteSizesOfTypes.UNIT
    else -> ByteSizesOfTypes.DEFAULT
}

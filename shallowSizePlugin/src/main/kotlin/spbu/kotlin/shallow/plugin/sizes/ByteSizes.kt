package spbu.kotlin.shallow.plugin.sizes

import org.jetbrains.kotlin.ir.types.*

object ByteSizes {
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
    isBoolean() -> ByteSizes.BOOLEAN
    isUnit() -> ByteSizes.UNIT
    else -> ByteSizes.DEFAULT
}

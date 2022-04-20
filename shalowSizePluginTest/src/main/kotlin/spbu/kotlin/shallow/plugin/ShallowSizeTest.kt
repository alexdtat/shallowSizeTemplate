package spbu.kotlin.shallow.plugin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


const val DEFAULT_SIZE = 8
const val BOOLEAN_SIZE = 1
const val FUNCTION_NAME = "shallowSize"

@Suppress("DEPRECATION")
class AddShallowSizeMethodTest {
    @ParameterizedTest(name = "case [{index}]: {0}")
    @MethodSource("getTestData")
    fun shallowSize(testedClass: Any, expectedSize: Int) {
        require(testedClass::class.isData) { "Only data classes have shallowSize method." }

        testedClass::class.members.find { it.name == FUNCTION_NAME }?.let {
            assertEquals(expectedSize, it.call(testedClass))
        } ?: throw IllegalArgumentException("There's no $FUNCTION_NAME method in this class.")
    }

    companion object {
        @JvmStatic
        fun getTestData() = listOf(
            Arguments.of(BaseClass("Hello"), DEFAULT_SIZE),
            Arguments.of(InternalClass(true), BOOLEAN_SIZE),
            Arguments.of(InheritInterfaces(3), Int.SIZE_BYTES),
            Arguments.of(InheritClass(3), Int.SIZE_BYTES),
            Arguments.of(NoBackField('c'), 2),
            Arguments.of(PrivateFields(3), Long.SIZE_BYTES + Int.SIZE_BYTES),
            Arguments.of(
                MultipleFieldsInConstructor(1, 2, 3, 4),
                Byte.SIZE_BYTES + Short.SIZE_BYTES + Int.SIZE_BYTES + Long.SIZE_BYTES
            ),
            Arguments.of(NullablePrimitives(1f, 1.0, 'c', true), 4 * DEFAULT_SIZE),
            Arguments.of(JavaCharacter(Character('3')), DEFAULT_SIZE),
            Arguments.of(NoExplicitType(3), Int.SIZE_BYTES + Long.SIZE_BYTES),
            Arguments.of(OverrideFieldFromClass(4), Int.SIZE_BYTES),
            Arguments.of(OverrideFieldFromInterface(4), Int.SIZE_BYTES)
        )
    }
}

package com.mobiroller.core.coreui.util.formatter

/*
* Usage
*
* val mask = Mask(
*     value = "+90 (___) ___ __ __",
*     character = '_',
*     style = MaskStyle.COMPLETABLE
* )
* val listener = MaskChangedListener(mask)
* textField.addTextChangedListener(listener)
*/

data class Mask(
        val value: String,
        val character: Char = value.mostOccurred(),
        val style: MaskStyle = MaskStyle.NORMAL
) {

    init {
        require(value.isNotEmpty()) { ERROR_EMPTY_VALUE }
        require(value.contains(character)) { ERROR_INVALID_CHARACTER }
    }

    companion object {
        const val ERROR_EMPTY_VALUE = "Mask cannot be empty"
        const val ERROR_INVALID_CHARACTER = "Mask does not contain specified character"
    }
}

/**
 * Returns the character with most occurrences
 */
internal fun String.mostOccurred(): Char {
    require(isNotEmpty()) { Mask.ERROR_EMPTY_VALUE }
    val result = groupBy { it }.maxByOrNull { it.value.size }
    return result?.key ?: first()
}
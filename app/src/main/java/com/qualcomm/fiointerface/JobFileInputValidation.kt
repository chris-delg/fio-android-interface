package com.qualcomm.fiointerface

import kotlin.math.pow

class JobFileInputValidation {
    private val kBaseVal: Double = 1000.0
    private val kiBaseVal: Double = 1024.0
    private val suffixValueMap: Map<String, Long> = mapOf(
        "k" to kBaseVal.toLong(),
        "m" to kBaseVal.pow(2).toLong(),
        "g" to kBaseVal.pow(3).toLong(),
        "t" to kBaseVal.pow(4).toLong(),
        "p" to kBaseVal.pow(5).toLong(),
        "ki" to kiBaseVal.toLong(),
        "mi" to kiBaseVal.pow(2).toLong(),
        "gi" to kiBaseVal.pow(3).toLong(),
        "ti" to kiBaseVal.pow(4).toLong(),
        "pi" to kiBaseVal.pow(5).toLong()
    )

    /*
    * Size cannot be smaller than block size.
    */
    fun validateSizeGreaterThanBlockSize(
        blockSize: String,
        blockSizeSuffix: String,
        size: String,
        sizeSuffix: String
    ): Boolean {
        if (blockSize.isNotBlank() and size.isNotBlank()) {
            return blockSize.toInt() * suffixValueMap[blockSizeSuffix]!! > size.toInt() * suffixValueMap[sizeSuffix]!!
        }
        return true
    }
}

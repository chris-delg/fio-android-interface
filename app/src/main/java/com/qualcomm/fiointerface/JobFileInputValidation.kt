package com.qualcomm.fiointerface

import android.util.Log
import kotlin.math.pow

class JobFileInputValidation {
    private val kBaseVal: Double = 1000.0
    private val kiBaseVal: Double = 1024.0
    private val suffixValueMap: Map<String, Long> = mapOf(
        "K" to kBaseVal.toLong(),
        "M" to kBaseVal.pow(2).toLong(),
        "G" to kBaseVal.pow(3).toLong(),
        "T" to kBaseVal.pow(4).toLong(),
        "P" to kBaseVal.pow(5).toLong(),
        "Ki" to kiBaseVal.toLong(),
        "Mi" to kiBaseVal.pow(2).toLong(),
        "Gi" to kiBaseVal.pow(3).toLong(),
        "Ti" to kiBaseVal.pow(4).toLong(),
        "Pi" to kiBaseVal.pow(5).toLong()
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
            return blockSize.toInt() * suffixValueMap[blockSizeSuffix]!! < size.toInt() * suffixValueMap[sizeSuffix]!!
        }
        return true
    }

    fun validateSizeNotEmptyWhenBlockSizeNotEmpty(blockSize: String, size: String): Boolean {
        return !(blockSize.isNotBlank() and size.isBlank())
    }
}

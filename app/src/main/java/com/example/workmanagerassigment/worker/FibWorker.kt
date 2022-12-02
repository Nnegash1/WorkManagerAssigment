package com.example.workmanagerassigment.worker


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanagerassigment.util.Constants
import kotlinx.coroutines.delay
import java.math.BigInteger

class FibWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val cache: MutableMap<Int, BigInteger> = mutableMapOf()
        val link = inputData.getString(Constants.KEY_IMAGE_URI) ?: ""
        val fib = fib(50, cache)
        Log.e("FibWorker", fib.toString())
        delay(3000L)
        val outputData = workDataOf(Constants.FIB_NUM to fib.toInt())
        return Result.success(outputData)
    }


    private fun fib(num: Int, cache: MutableMap<Int, BigInteger>): BigInteger {
        if (cache.containsKey(num)) {
            return cache[num]!!
        }
        if (num <= 2) {
            return BigInteger.ONE
        }
        cache[num] = fib(num - 1, cache) + fib(num - 2, cache)
        return cache[num]!!
    }
}

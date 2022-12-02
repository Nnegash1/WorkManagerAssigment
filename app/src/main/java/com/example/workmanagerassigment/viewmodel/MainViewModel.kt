package com.example.workmanagerassigment.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.workmanagerassigment.util.Constants
import com.example.workmanagerassigment.worker.FibWorker
import com.example.workmanagerassigment.worker.ImgurWorker

class MainViewModel : ViewModel() {

    private val _workInfoData = MutableLiveData<WorkInfo>()
    val workInfoData get() = _workInfoData
    private lateinit var liveData: LiveData<WorkInfo>

    private val observer =
        Observer<WorkInfo> { t -> Log.e("TEST", t?.outputData.toString()) }

    fun startWork(context: Context, selectImage: Uri) {
        val imageData = workDataOf(Constants.KEY_IMAGE_URI to selectImage.toString())
        val uploadWorkRequest = OneTimeWorkRequestBuilder<ImgurWorker>()
            .setInputData(imageData)
            .build()
        val fibWorker = OneTimeWorkRequest.from(FibWorker::class.java)
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "IMAGE_UPLOADER",
                ExistingWorkPolicy.REPLACE,
                uploadWorkRequest
            )
            .then(fibWorker)
            .enqueue()
        liveData = WorkManager.getInstance(context).getWorkInfoByIdLiveData(fibWorker.id)
        liveData.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        liveData.removeObserver(observer)
    }

}

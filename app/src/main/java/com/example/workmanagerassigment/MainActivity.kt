package com.example.workmanagerhomework1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.workmanagerhomework1.ui.theme.WorkManagerHomework1Theme
import com.example.workmanagerhomework1.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<MainViewModel>()
        super.onCreate(savedInstanceState)
        setContent {
            WorkManagerHomework1Theme() {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ShowScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ShowScreen(
    viewModel: MainViewModel
) {
    // viewModel.workInfoData.observeAsState().value
    val context = LocalContext.current
    var selectImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectImage = uri
    }
    Scaffold(content = {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Display Image
            DisplayImage(selectImages = selectImage, context = context)
            // Buttons
            Row() {
                Button(onClick = {
                    launcher.launch("image/*")
                }) {
                    Text(text = "Selected a photo")
                }
                Spacer(modifier = Modifier.width(10.dp))
                selectImage?.let {
                    Button(onClick = {
                        viewModel.startWork(context, selectImage!!)
                    }) {
                        Text(text = "Upload photo")
                    }
                }
            }
        }
    })
}

@Composable
fun DisplayImage(
    selectImages: Uri?,
    context: Context
) {
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    selectImages?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
        bitmap.value?.let { btm ->
            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )
        }
    }
}


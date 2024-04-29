package com.sagittarius
import com.sagittarius.OpenCVHelper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import org.opencv.imgproc.Imgproc
/*
* Native module class to control lighting properties in an image.
* Applies exposure, contrast, shadow, and highlight adjustments.
* Utilizes [OpenCVHelper] for image processing.
* */
class LightingControls(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    // Store the current image in memory.
    private var currentImage: Bitmap? = null

    // OpenCV Helper instance.
    private lateinit var openCVHelper: OpenCVHelper

    // Store the exposure value for later use.
    private var exposureValue: Double? = null

    // Return the module name for React Native.
    override fun getName() = "LightingControls"

    /*
    * Native method to change exposure of an image.
    * */
    @ReactMethod
    fun changeExposure(beta: Double, callback: Callback) {
        currentImage?.let { image ->
            Log.d("Lighting Controls", "Alpha value received: $beta")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform exposure adjustment in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.adjustBrightnessAsync(image, beta)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve exposure value
                exposureValue = beta

                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }
// In the LightingControls class

@ReactMethod
fun changeContrast(contrastFactor: Double, callback: Callback) {
    currentImage?.let { image ->
        Log.d("Lighting Controls", "Contrast factor received: $contrastFactor")

        // Initialize OpenCVHelper if not already initialized
        if (!::openCVHelper.isInitialized) {
            openCVHelper = OpenCVHelper()
        }

        // Perform contrast adjustment with histogram equalization in parallel using Kotlin coroutines
        CoroutineScope(Dispatchers.Default).launch {
            val changedBitmap = openCVHelper.adjustContrastWithHistogramEqualizationAsync(image, contrastFactor)
            val base64String = bitmapToBase64(changedBitmap)

            withContext(Dispatchers.Main) {
                callback.invoke(base64String)
            }
        }
    } ?: Log.e("Lighting Controls", "Current image is null")
}
@ReactMethod
fun changeSharpness(strength: Double, callback: Callback) {
    currentImage?.let { image ->
        Log.d("Lighting Controls", "Sharpness strength received: $strength")

        // Initialize OpenCVHelper if not already initialized
        if (!::openCVHelper.isInitialized) {
            openCVHelper = OpenCVHelper()
        }

        // Perform sharpness adjustment in parallel using Kotlin coroutines
        CoroutineScope(Dispatchers.Default).launch {
            val sharpenedBitmap = openCVHelper.sharpenImage(image, strength)
            val base64String = bitmapToBase64(sharpenedBitmap)

            withContext(Dispatchers.Main) {
                callback.invoke(base64String)
            }
        }
    } ?: Log.e("Lighting Controls", "Current image is null")
}

/*
* Suspend method to execute changeSharpness on a different coroutine.
* */
private suspend fun OpenCVHelper.sharpenImageAsync(bitmap: Bitmap, strength: Double): Bitmap {
    return withContext(Dispatchers.Default) {
        sharpenImage(bitmap, strength)
    }
}



    /*
    * Suspend method to execute changeExposure on a different coroutine.
    * */
    private suspend fun OpenCVHelper.adjustBrightnessAsync(bitmap: Bitmap, beta: Double): Bitmap {
        return withContext(Dispatchers.Default) {
            adjustBrightness(bitmap, beta)
        }
    }
    private suspend fun OpenCVHelper.adjustSharpnessAsync(bitmap: Bitmap, beta: Double): Bitmap {
        return withContext(Dispatchers.Default) {
            adjustBrightness(bitmap, beta)
        }
    }
// In the OpenCVHelper class

suspend fun OpenCVHelper.adjustContrastWithHistogramEqualizationAsync(bitmap: Bitmap, contrastFactor: Double): Bitmap {
    return withContext(Dispatchers.Default) {
        adjustContrastWithHistogramEqualization(bitmap, contrastFactor)
    }
}






    /*
    * Method to convert bitmap to base64 string.
    * The base64 string is used to display the image in the React Native app.
    * */
    private fun bitmapToBase64(bitmap: Bitmap): String {

        // Downscale the image preview for faster processing.
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap, bitmap.width / 2, bitmap.height / 2, true
        )

        // Use JPEG compression with lower quality for faster encoding.
        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream)

        // Returning the base64 string representation of the image.
        val byteArray = byteArrayOutputStream.toByteArray()
        Log.d("Lighting Controls", "Base64 encoding successful")
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /*
    * Read image from the specified URI.
    * The image is stored in the currentImage property.
    * */
    @ReactMethod(isBlockingSynchronousMethod = true)
    fun readImage(uri: String, callback: Callback) {
        val image = BitmapFactory.decodeFile(uri)

        currentImage = image

        if (currentImage == null) {
            Log.e("Lighting Controls", "Failed to read image")
            callback.invoke("Failed to read image")
            return
        } else {
            Log.d("Lighting Controls", "Image read successfully")
            callback.invoke("Image read successfully")
        }
    }
}

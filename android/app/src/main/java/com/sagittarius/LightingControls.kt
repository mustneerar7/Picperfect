package com.sagittarius

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
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.collections.mutableMapOf

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

    // Store current control name
    private var currentControl: LightingProperty? = null
    private enum class LightingProperty {
        EXPOSURE, CONTRAST, SHADOW, HIGHLIGHT, MIDTONES
    }

    // Hashtable to store the controls and their values
    private val controlValues = mutableMapOf<String, Double>()

    // Return the module name for React Native.
    override fun getName() = "LightingControls"


    // Method which restores the values in the controlValues hashtable when a control is changed / selected
//    fun restoreControlValues(control: String) {
//        controlValues[control]?.let { value ->
//            when (control) {
//                "exposure" -> changeExposure(value) {},
//                "shadows" -> changeShadows(value.toFloat()) {},
//                "midtone" -> changeMidtones(value.toFloat()) {},
//                "highlight" -> changeHighlights(value.toFloat()) {}
//            }
//        }
//    }

    /*
    * Native method to change exposure of an image.
    * */
    @ReactMethod
    fun changeExposure(beta: Double, callback: Callback) {

                        // Store the current control name
                currentControl = LightingProperty.EXPOSURE

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

                // save the current control value
                controlValues["exposure"] = beta

//                // if current control is exposure, donot update the original image
               if (currentControl != LightingProperty.EXPOSURE) {
                   currentImage = changedBitmap
               }



                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    /*
    * Suspend method to execute changeExposure on a different coroutine.
    * */
    private suspend fun OpenCVHelper.adjustBrightnessAsync(bitmap: Bitmap, beta: Double): Bitmap {
        return withContext(Dispatchers.Default) {
            adjustBrightness(bitmap, beta)
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

    /* change shadows */


    @ReactMethod
    fun changeShadows(alpha: Float, callback: Callback) {

                        // Store the current control name
                currentControl = LightingProperty.SHADOW

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Alpha value received: $alpha")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform shadows adjustment in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.changeShadowsAsync(image, alpha)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve shadows value
                controlValues["shadows"] = alpha.toDouble()

                // if current control is shadows, donot update the original image
               if (currentControl != LightingProperty.SHADOW) {
                   currentImage = changedBitmap
               }



                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    private suspend fun OpenCVHelper.changeShadowsAsync(bitmap: Bitmap, alpha: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            changeShadows(bitmap, alpha)
        }
    }

// Midtones

    @ReactMethod
    fun changeMidtones(midtoneShift: Float, callback: Callback) {


                        // Store the current control name
                currentControl = LightingProperty.MIDTONES

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Midtone shift value received: $midtoneShift")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform midtone adjustment in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.changeMidtonesAsync(image, midtoneShift)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve midtone shift value
                controlValues["midtone"] = midtoneShift.toDouble()

                // if current control is midtones, donot update the original image
               if (currentControl != LightingProperty.MIDTONES) {
                   currentImage = changedBitmap
               }


                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    /*
    * Suspend method to execute changeMidtones on a different coroutine.
    * */
    private suspend fun OpenCVHelper.changeMidtonesAsync(bitmap: Bitmap, midtoneShift: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            changeMidtones(bitmap, midtoneShift)
        }
    }

//     Highlights

    @ReactMethod
    fun changeHighlights(highlightShift: Float, callback: Callback) {

                        // Store the current control name
                currentControl = LightingProperty.HIGHLIGHT

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Highlight shift value received: $highlightShift")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform highlight adjustment in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.changeHighlightsAsync(image, highlightShift)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve highlight shift value
                controlValues["highlight"] = highlightShift.toDouble()

            //     if current control is highlight, donot update the original image
               if (currentControl != LightingProperty.HIGHLIGHT) {
                   currentImage = changedBitmap
               }



                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    /*
    * Suspend method to execute changeHighlights on a different coroutine.
    * */
    private suspend fun OpenCVHelper.changeHighlightsAsync(bitmap: Bitmap, highlightShift: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            changeHighlights(bitmap, highlightShift)
        }
    }



}

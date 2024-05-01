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
import java.io.File
import android.os.Environment
import java.io.FileOutputStream
import java.util.UUID

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
        EXPOSURE, CONTRAST, SHADOW, HIGHLIGHT, MIDTONES ,NOISE,UNSHARP_MASK, CROP, ROTATE, FLIP
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

                // Store the current control name
                currentControl = LightingProperty.SHADOW


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

                // Store the current control name
                currentControl = LightingProperty.MIDTONES

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

                // Store the current control name
                currentControl = LightingProperty.HIGHLIGHT



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

    // function to compress the image from react native
    @ReactMethod
    fun compressImage(callback: Callback) {

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Compressing image")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform image compression in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val compressedBitmap = openCVHelper.compressImageAsync(image)

                // write the compressed image as a file to external storage
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "compressed_image_${UUID.randomUUID()}.jpg"
                )

                Log.d("Location", file.absolutePath)

                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())
                callback.invoke("Image compressed")

            }
        } ?: Log.e("Lighting Controls", "Current image is null")

    }

    /*
    * Suspend method to execute compressImage on a different coroutine.
     */
    private suspend fun OpenCVHelper.compressImageAsync(bitmap: Bitmap): Bitmap {
        return withContext(Dispatchers.Default) {
            compressImage(bitmap)
        }
    }

    // Method to remove noise from image

    @ReactMethod
    fun reduceNoise(noiseReductionFactor: Float, callback: Callback) {
        // Store the current control name
        currentControl = LightingProperty.NOISE

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Noise reduction factor received: $noiseReductionFactor")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform noise reduction in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.reduceNoiseAsync(image, noiseReductionFactor)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve noise reduction factor
                controlValues["noise"] = noiseReductionFactor.toDouble()

                // if current control is noise, don't update the original image
                if (currentControl != LightingProperty.NOISE) {
                    currentImage = changedBitmap
                }

                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    /*
    * Suspend method to execute reduceNoise on a different coroutine.
    * */
    private suspend fun OpenCVHelper.reduceNoiseAsync(bitmap: Bitmap, noiseReductionFactor: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            reduceNoise(bitmap, noiseReductionFactor)
        }
    }

    // Unsharp masking

    @ReactMethod
    fun unsharpMask(blurFactor: Float, callback: Callback) {
        // Store the current control name
        currentControl = LightingProperty.UNSHARP_MASK

        currentImage?.let { image ->
            Log.d("Lighting Controls", "Blur factor received: $blurFactor")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform unsharp masking in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val changedBitmap = openCVHelper.unsharpMaskAsync(image, blurFactor)
                val base64String = bitmapToBase64(changedBitmap)

                // Preserve blur factor
                controlValues["unsharp_mask"] = blurFactor.toDouble()

                // if current control is unsharp_mask, don't update the original image
                if (currentControl != LightingProperty.UNSHARP_MASK) {
                    currentImage = changedBitmap
                }

                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    /*
    * Suspend method to execute unsharpMask on a different coroutine.
    * */
    private suspend fun OpenCVHelper.unsharpMaskAsync(bitmap: Bitmap, blurFactor: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            unsharpMask(bitmap, blurFactor)
        }
    }



    @ReactMethod
    fun cropImage(aspectRatio: Float, x: Int, y: Int, callback: Callback) {
        currentImage?.let { image ->
            Log.d("Lighting Controls", "Cropping image")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform image cropping in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {

                // get the width and height of the image automatically
                val width = image.width
                val height = image.height

                val croppedBitmap = openCVHelper.cropImageAsync(image, x, y, width, height, aspectRatio)
                val base64String = bitmapToBase64(croppedBitmap)

                // save the current control value
                controlValues["crop"] = aspectRatio.toDouble()

                // if current control is crop, donot update the original image
                if (currentControl != LightingProperty.CROP) {
                    currentImage = croppedBitmap
                }

                // Store the current control name
                currentControl = LightingProperty.CROP


                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    private suspend fun OpenCVHelper.cropImageAsync(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int, aspectRatio: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            cropImage(aspectRatio, x, y, width, height, bitmap)
        }
    }

    @ReactMethod
    fun rotateImage(angle: Float, callback: Callback) {
        currentImage?.let { image ->
            Log.d("Lighting Controls", "Rotating image")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform image rotation in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val rotatedBitmap = openCVHelper.rotateImageAsync(image, angle)
                val base64String = bitmapToBase64(rotatedBitmap)

                // if current control is crop, donot update the original image
                if (currentControl != LightingProperty.ROTATE) {
                    currentImage = rotatedBitmap
                }

                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    private suspend fun OpenCVHelper.rotateImageAsync(bitmap: Bitmap, angle: Float): Bitmap {
        return withContext(Dispatchers.Default) {
            rotateImage(bitmap, angle)
        }
    }

    @ReactMethod
    fun flipImage(callback: Callback) {
        currentImage?.let { image ->
            Log.d("Lighting Controls", "Flipping image")

            // Initialize OpenCVHelper if not already initialized
            if (!::openCVHelper.isInitialized) {
                openCVHelper = OpenCVHelper()
            }

            // Perform image flipping in parallel using Kotlin coroutines
            CoroutineScope(Dispatchers.Default).launch {
                val flippedBitmap = openCVHelper.flipImageAsync(image)
                val base64String = bitmapToBase64(flippedBitmap)

                // if current control is crop, donot update the original image
                if (currentControl != LightingProperty.FLIP) {
                    currentImage = flippedBitmap
                }

                withContext(Dispatchers.Main) {
                    callback.invoke(base64String)
                }
            }
        } ?: Log.e("Lighting Controls", "Current image is null")
    }

    private suspend fun OpenCVHelper.flipImageAsync(bitmap: Bitmap): Bitmap {
        return withContext(Dispatchers.Default) {
            flipImage(bitmap)
        }
    }



}

package com.sagittarius

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Range
import org.opencv.core.Scalar
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Deferred
import org.opencv.imgproc.Imgproc
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.math.abs



/* Helper class to perform image processing using OpenCV. */
class OpenCVHelper {

    /* Adjust the brightness of an image using OpenCV. */
    suspend fun adjustBrightness(bitmap: Bitmap, gamma: Double): Bitmap =
        withContext(Dispatchers.Default) {

            // Convert the input bitmap to a Mat object
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)

            // Convert the Mat object to a floating-point Mat object
            val floatMat = Mat()
            mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

            // Apply gamma correction
            Core.pow(floatMat, 1 / gamma, floatMat)

            // Convert the floating-point Mat object back to a Mat object of type CV_8U
            floatMat.convertTo(mat, CvType.CV_8U, 255.0)

            // Convert the Mat object back to a bitmap
            val resultBitmap =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Lighting CV", "Exposure changed")
            resultBitmap
        }

    /* Helper method to process matrix in parallel */
    private fun processChunk(chunkMat: Mat, alpha: Float) {
        Core.multiply(chunkMat, Scalar.all(alpha.toDouble()), chunkMat)
    }

    /* change shadows */

    suspend fun changeShadows(bitmap: Bitmap, alpha: Float): Bitmap =
        withContext(Dispatchers.Default) {

            // Convert the input bitmap to a Mat object
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)

            // Convert the Mat object to a floating-point Mat object
            val floatMat = Mat()
            mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

            // Determine the number of threads to use for parallel processing
            val numThreads = Runtime.getRuntime().availableProcessors()
            val rowsPerThread = floatMat.rows() / numThreads
            val jobList = mutableListOf<Deferred<Unit>>()

            // Split the image into equal-sized chunks and process each chunk concurrently
            var startY = 0
            for (i in 0 until numThreads) {
                val endY = if (i == numThreads - 1) floatMat.rows() else startY + rowsPerThread
                val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, floatMat.cols()))

                val job = async {
                    processChunkShadows(chunkMat, alpha)
                }
                jobList.add(job)

                startY = endY
            }

            // Wait for all threads to complete
            jobList.awaitAll()

            // Convert the floating-point Mat object back to a Mat object unit8
            floatMat.convertTo(mat, CvType.CV_8U, 255.0)

            // Convert the Mat object back to a bitmap
            val resultBitmap =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Lighting CV", "Shadows changed")
            resultBitmap
        }

    private fun processChunkShadows(chunkMat: Mat, alpha: Float) {
        for (i in 0 until chunkMat.rows()) {
            for (j in 0 until chunkMat.cols()) {
                val pixel = chunkMat.get(i, j)
                if (pixel.all { it > 0 && it <= 85 }) {
                    val newPixel = pixel.map { it * alpha }.toDoubleArray()
                    chunkMat.put(i, j, *newPixel)
                }
            }
        }
    }


    /* Change the midtones of an image using OpenCV. */

    suspend fun changeMidtones(bitmap: Bitmap, midtoneShift: Float): Bitmap =
        withContext(Dispatchers.Default) {

            // Convert the input bitmap to a Mat object
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)

            // Convert the Mat object to a floating-point Mat object
            val floatMat = Mat()
            mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

            // Determine the number of threads to use for parallel processing
            val numThreads = Runtime.getRuntime().availableProcessors()
            val rowsPerThread = floatMat.rows() / numThreads
            val jobList = mutableListOf<Deferred<Unit>>()

            // Split the image into equal-sized chunks and process each chunk concurrently
            var startY = 0
            for (i in 0 until numThreads) {
                val endY = if (i == numThreads - 1) floatMat.rows() else startY + rowsPerThread
                val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, floatMat.cols()))

                val job = async {
                    processChunkMidtones(chunkMat, midtoneShift)
                }
                jobList.add(job)

                startY = endY
            }

            // Wait for all threads to complete
            jobList.awaitAll()

            // Convert the floating-point Mat object back to a Mat object unit8
            floatMat.convertTo(mat, CvType.CV_8U, 255.0)

            // Convert the Mat object back to a bitmap
            val resultBitmap =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Lighting CV", "Midtones changed")
            resultBitmap
        }

    private fun processChunkMidtones(chunkMat: Mat, midtoneShift: Float) {
        for (i in 0 until chunkMat.rows()) {
            for (j in 0 until chunkMat.cols()) {
                val pixel = chunkMat.get(i, j)
                if (pixel.all { it > 85 && it < 170 }) {
                    val newPixel = pixel.map { it * midtoneShift }.toDoubleArray()
                    chunkMat.put(i, j, *newPixel)
                }
            }
        }
    }

//  Highlights


    suspend fun changeHighlights(bitmap: Bitmap, highlightShift: Float): Bitmap =
        withContext(Dispatchers.Default) {

            // Convert the input bitmap to a Mat object
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)

            // Convert the Mat object to a floating-point Mat object
            val floatMat = Mat()
            mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

            // Determine the number of threads to use for parallel processing
            val numThreads = Runtime.getRuntime().availableProcessors()
            val rowsPerThread = floatMat.rows() / numThreads
            val jobList = mutableListOf<Deferred<Unit>>()

            // Split the image into equal-sized chunks and process each chunk concurrently
            var startY = 0
            for (i in 0 until numThreads) {
                val endY = if (i == numThreads - 1) floatMat.rows() else startY + rowsPerThread
                val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, floatMat.cols()))

                val job = async {
                    processChunkHighlights(chunkMat, highlightShift)
                }
                jobList.add(job)

                startY = endY
            }

            // Wait for all threads to complete
            jobList.awaitAll()

            // Convert the floating-point Mat object back to a Mat object unit8
            floatMat.convertTo(mat, CvType.CV_8U, 255.0)


            // Convert the Mat object back to a bitmap
            val resultBitmap =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Lighting CV", "Highlights changed")
            resultBitmap
        }

    private fun processChunkHighlights(chunkMat: Mat, highlightShift: Float) {
        for (i in 0 until chunkMat.rows()) {
            for (j in 0 until chunkMat.cols()) {
                val pixel = chunkMat.get(i, j)
                if (pixel.all { it > 170 && it <= 255 }) {
                    val newPixel = pixel.map { it * highlightShift }.toDoubleArray()
                    chunkMat.put(i, j, *newPixel)
                }
            }
        }
    }

    /*
    * A function to compress the image by eeleminating last 4 bits of each pixel value.
    * */

    suspend fun compressImage(bitmap: Bitmap): Bitmap = withContext(Dispatchers.Default) {
        // Convert the input bitmap to a Mat object
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Convert the Mat object to a floating-point Mat object
        val floatMat = Mat()
        mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

        // Process the image to remove the last 4 bits of each pixel value
        for (row in 0 until mat.rows()) {
            for (col in 0 until mat.cols()) {
                val pixel = mat.get(row, col)
                val newPixel = DoubleArray(pixel.size)

                for (i in pixel.indices) {
                    newPixel[i] = (pixel[i].toInt() and 0xF0) / 255.0
                }

                mat.put(row, col, *newPixel)
            }
        }

        // Convert the floating-point Mat object back to a Mat object unit8
        floatMat.convertTo(mat, CvType.CV_8U, 255.0)

        // Convert the Mat object back to a bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, resultBitmap)

        // Release the Mats to free up memory
        mat.release()
        floatMat.release()

        Log.d("Lighting CV", "Image compressed")
        resultBitmap
    }

    // method to remove Noise
    suspend fun reduceNoise(bitmap: Bitmap, noiseReductionFactor: Float): Bitmap = withContext(Dispatchers.Default) {
        // Convert the input bitmap to a Mat object
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Convert the Mat object to a floating-point Mat object
        val floatMat = Mat()
        mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

        // Determine the number of threads to use for parallel processing
        val numThreads = Runtime.getRuntime().availableProcessors()
        val rowsPerThread = floatMat.rows() / numThreads
        val jobList = mutableListOf<Deferred<Unit>>()

        // Split the image into equal-sized chunks and process each chunk concurrently
        var startY = 0
        for (i in 0 until numThreads) {
            val endY = if (i == numThreads - 1) floatMat.rows() else startY + rowsPerThread
            val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, floatMat.cols()))

            val job = async {
                processChunkNoise(chunkMat, noiseReductionFactor)
            }
            jobList.add(job)

            startY = endY
        }

        // Wait for all threads to complete
        jobList.awaitAll()

        // Convert the floating-point Mat object back to a Mat object unit8
        floatMat.convertTo(mat, CvType.CV_8U, 255.0)

        // Convert the Mat object back to a bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, resultBitmap)

        // Release the Mats to free up memory
        mat.release()
        floatMat.release()

        Log.d("Lighting CV", "Noise removed")
        resultBitmap
    }

    private fun processChunkNoise(chunkMat: Mat, noiseReductionFactor: Float) {
        val kernelSize = Math.max(1, Math.round(noiseReductionFactor / 2) * 2 + 1)
        Imgproc.GaussianBlur(chunkMat, chunkMat, org.opencv.core.Size(kernelSize.toDouble(), kernelSize.toDouble()), 0.0)

    }

//     unsharp masking

    suspend fun unsharpMask(bitmap: Bitmap, blurFactor: Float): Bitmap = withContext(Dispatchers.Default) {
        // Convert the input bitmap to a Mat object
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Convert the Mat object to a floating-point Mat object
        val floatMat = Mat()
        mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

        // Determine the number of threads to use for parallel processing
        val numThreads = Runtime.getRuntime().availableProcessors()
        val rowsPerThread = floatMat.rows() / numThreads
        val jobList = mutableListOf<Deferred<Unit>>()

        // Split the image into equal-sized chunks and process each chunk concurrently
        var startY = 0
        for (i in 0 until numThreads) {
            val endY = if (i == numThreads - 1) floatMat.rows() else startY + rowsPerThread
            val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, floatMat.cols()))

            val job = async {
                processChunkUnsharpMask(chunkMat, blurFactor)
            }
            jobList.add(job)

            startY = endY
        }

        // Wait for all threads to complete
        jobList.awaitAll()

        // Convert the floating-point Mat object back to a Mat object unit8
        floatMat.convertTo(mat, CvType.CV_8U, 255.0)

        // Convert the Mat object back to a bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, resultBitmap)

        // Release the Mats to free up memory
        mat.release()
        floatMat.release()

        Log.d("Lighting CV", "Unsharp masking done")
        resultBitmap
    }

    private fun processChunkUnsharpMask(chunkMat: Mat, blurFactor: Float) {
        val kernelSize = Math.max(1, Math.round(blurFactor / 2) * 2 + 1)
        val blurred = Mat()
        Imgproc.GaussianBlur(chunkMat, blurred, org.opencv.core.Size(kernelSize.toDouble(), kernelSize.toDouble()), 0.0)
        val unsharpMask = Mat()
        Core.subtract(chunkMat, blurred, unsharpMask)
        Core.add(chunkMat, unsharpMask, chunkMat)
    }







    fun cropImage(
        desiredAspectRatio: Float,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        bitmap: Bitmap
    ): Bitmap {
        val originalAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val croppedWidth: Int
        val croppedHeight: Int
        val xOffset: Int
        val yOffset: Int

        if (originalAspectRatio > desiredAspectRatio) {
            // Original image is wider, adjust height
            croppedWidth = bitmap.height * desiredAspectRatio.toInt()
            croppedHeight = bitmap.height
            xOffset = x + (width - croppedWidth) / 2
            yOffset = y
        } else {
            // Original image is taller, adjust width
            croppedWidth = bitmap.width
            croppedHeight = (bitmap.width / desiredAspectRatio).toInt()
            xOffset = x
            yOffset = y + (height - croppedHeight) / 2
        }

        if (xOffset < 0 || yOffset < 0 || xOffset + croppedWidth > bitmap.width || yOffset + croppedHeight > bitmap.height) {
            // Out of bounds, return null or handle appropriately
            return bitmap
        }

        val croppedBitmap =
            Bitmap.createBitmap(bitmap, xOffset, yOffset, croppedWidth, croppedHeight)
        return croppedBitmap
    }

    // Function to rotate the image at given angle
    fun rotateImage(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // Function to flip the image horizontally
    fun flipImage(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}

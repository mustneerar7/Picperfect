package com.sagittarius

import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Range
import org.opencv.core.Scalar

/* Helper class to perform image processing using OpenCV. */
class OpenCVHelper {

  /* Change the exposure of an image using OpenCV. */
  suspend fun changeExposure(bitmap: Bitmap, alpha: Float): Bitmap =
    withContext(Dispatchers.Default) {

      // Convert the input bitmap to a Mat object
      val mat = Mat()
      Utils.bitmapToMat(bitmap, mat)

      // Convert the Mat object to a floating-point Mat object
      val floatMat = Mat()
      mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

      // Determine the number of threads to use for parallel processing
      val numThreads = Runtime.getRuntime().availableProcessors()
      val rowsPerThread = bitmap.height / numThreads
      val jobList = mutableListOf<Deferred<Unit>>()

      // Split the image into equal-sized chunks and process each chunk concurrently
      var startY = 0
      for (i in 0 until numThreads) {
        val endY = if (i == numThreads - 1) bitmap.height else startY + rowsPerThread
        val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, bitmap.width))

        val job = async {
          processChunk(chunkMat, alpha)
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

      // Process the image to change shadows
      processShadows(floatMat, alpha)

      // Convert the floating-point Mat object back to a Mat object unit8
      floatMat.convertTo(mat, CvType.CV_8U, 255.0)

      // Convert the Mat object back to a bitmap
      val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
      Utils.matToBitmap(mat, resultBitmap)

      // Release the Mats to free up memory
      mat.release()
      floatMat.release()

      Log.d("Lighting CV", "Shadows changed")
      resultBitmap
    }

  private fun processShadows(chunkMat: Mat, alpha: Float) {
    // Create a mask where the pixel intensity is within the shadow range (35 to 85)
    val lowerThreshold = 35.0
    val upperThreshold = 85.0
    val mask = Mat()
    Core.inRange(chunkMat, Scalar(lowerThreshold, lowerThreshold, lowerThreshold), Scalar(upperThreshold, upperThreshold, upperThreshold), mask)

    // Create a Mat of the same size filled with the alpha value
    val alphaMat = Mat(chunkMat.size(), chunkMat.type(), Scalar(alpha.toDouble()))

    // Add the alpha value to the pixels in the mask
    Core.add(chunkMat, alphaMat, chunkMat, mask)

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
      val rowsPerThread = bitmap.height / numThreads
      val jobList = mutableListOf<Deferred<Unit>>()

      // Split the image into equal-sized chunks and process each chunk concurrently
      var startY = 0
      for (i in 0 until numThreads) {
        val endY = if (i == numThreads - 1) bitmap.height else startY + rowsPerThread
        val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, bitmap.width))

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
      val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
      Utils.matToBitmap(mat, resultBitmap)

      // Release the Mats to free up memory
      mat.release()
      floatMat.release()

      Log.d("Lighting CV", "Midtones changed")
      resultBitmap
    }

  /* Helper method to process matrix in parallel */
  private fun processChunkMidtones(chunkMat: Mat, midtoneShift: Float) {
    Core.add(chunkMat, Scalar.all(midtoneShift.toDouble()), chunkMat)
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
      val rowsPerThread = bitmap.height / numThreads
      val jobList = mutableListOf<Deferred<Unit>>()

      // Split the image into equal-sized chunks and process each chunk concurrently
      var startY = 0
      for (i in 0 until numThreads) {
        val endY = if (i == numThreads - 1) bitmap.height else startY + rowsPerThread
        val chunkMat = Mat(floatMat, Range(startY, endY), Range(0, bitmap.width))

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
      val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
      Utils.matToBitmap(mat, resultBitmap)

      // Release the Mats to free up memory
      mat.release()
      floatMat.release()

      Log.d("Lighting CV", "Highlights changed")
      resultBitmap
    }

  /* Helper method to process matrix in parallel */
  private fun processChunkHighlights(chunkMat: Mat, highlightShift: Float) {
    Core.add(chunkMat, Scalar.all(highlightShift.toDouble()), chunkMat)
  }


}

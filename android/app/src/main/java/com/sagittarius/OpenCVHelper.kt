package com.sagittarius

import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar

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
            Core.pow(floatMat, gamma, floatMat)

            // Convert the floating-point Mat object back to a Mat object of type CV_8U
            floatMat.convertTo(mat, CvType.CV_8U, 255.0)

            // Convert the Mat object back to a bitmap
            val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Brightness with Gamma", "Adjusted")

            resultBitmap
        }


    /* Adjust the contrast of an image using OpenCV. */
    suspend fun adjustContrast(bitmap: Bitmap, alpha: Float): Bitmap =
        withContext(Dispatchers.Default) {

            // Convert the input bitmap to a Mat object
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)

            // Convert the Mat object to a floating-point Mat object
            val floatMat = Mat()
            mat.convertTo(floatMat, CvType.CV_32F, 1.0 / 255.0)

            // Apply contrast adjustment
            Core.multiply(floatMat, Scalar.all(alpha.toDouble()), floatMat)

            // Convert the floating-point Mat object back to a Mat object of type CV_8U
            floatMat.convertTo(mat, CvType.CV_8U)

            // Convert the Mat object back to a bitmap
            val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)

            // Release the Mats to free up memory
            mat.release()
            floatMat.release()

            Log.d("Contrast", "Adjusted")

            resultBitmap
        }
}

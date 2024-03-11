package com.sagittarius

import android.util.Log
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class LightingControls(
  reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {
  override fun getName() = "LightingControls"
  @ReactMethod
  fun changeExposure(alpha: Float, callback:Callback) {
    Log.d("Lighting Controls", "Alpha value received: $alpha")
    callback.invoke(null, "Exposure changed to $alpha")
  }
}
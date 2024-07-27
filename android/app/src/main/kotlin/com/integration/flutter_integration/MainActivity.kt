package com.integration.flutter_integration

import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val channel = "com.integration.flutter_integration/device"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channel
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getDeviceInfo" -> {
                    val deviceInfo = getDeviceInfo()
                    result.success(deviceInfo)
                }
                "getVideoInfo" -> {
                    // TODO: Implement video info extraction.
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun getDeviceInfo(): String {
        return "Device: ${Build.DEVICE}, OS Version: ${Build.VERSION.RELEASE}, Brand: ${Build.BRAND}"
    }
}

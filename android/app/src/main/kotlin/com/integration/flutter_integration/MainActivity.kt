package com.integration.flutter_integration

import android.media.MediaMetadataRetriever
import android.net.Uri
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
                    val videoInfo = getVideoInfo()
                    result.success(videoInfo)
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun getDeviceInfo(): String {
        return """
            Device: ${Build.DEVICE}
            Model: ${Build.MODEL}
            Manufacturer: ${Build.MANUFACTURER}
            OS Version: ${Build.VERSION.RELEASE}
            SDK Version: ${Build.VERSION.SDK_INT}
            Board: ${Build.BOARD}
            Brand: ${Build.BRAND}
            Display: ${Build.DISPLAY}
            """.trimIndent()
    }

    private fun getVideoInfo(): String {
        val retriever = MediaMetadataRetriever()

        // Path to sample_video.mp4 inside raw directory
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sample_video)
        retriever.setDataSource(applicationContext, videoUri)

        val durationMs =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                ?: 0L
        val durationSeconds = durationMs / 1000
        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

        retriever.release()

        return """
            Duration: $durationSeconds seconds
            Resolution: ${width}x$height
            Bitrate: $bitrate bps
            """.trimIndent()
    }
}

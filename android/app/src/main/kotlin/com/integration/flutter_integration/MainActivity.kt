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

    private fun getDeviceInfo(): Map<String, String> {
        return mapOf(
            "Device" to Build.DEVICE,
            "Product" to Build.PRODUCT,
            "Model" to Build.MODEL,
            "Manufacturer" to Build.MANUFACTURER,
            "OS Version" to Build.VERSION.RELEASE,
            "SDK Version" to Build.VERSION.SDK_INT.toString(),
            "Board" to Build.BOARD,
            "Brand" to Build.BRAND,
            "Hardware" to Build.HARDWARE,
            "User" to Build.USER,
            "Display" to Build.DISPLAY
        )
    }

    private fun getVideoInfo(): Map<String, String> {
        val retriever = MediaMetadataRetriever()
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sample_video)
        retriever.setDataSource(applicationContext, videoUri)

        val durationMs =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                ?: 0L
        val durationMinutes = durationMs / 60000
        val durationSeconds = (durationMs % 60000) / 1000
        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        val mimeType = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        val videoRotation =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)

        retriever.release()

        return mapOf(
            "Duration" to "${durationMinutes}m ${durationSeconds}s",
            "Resolution" to "${width}x$height",
            "Bitrate" to "$bitrate bps",
            "MIME Type" to "$mimeType",
            "Video Rotation" to "$videoRotation",
            "Saved Path" to videoUri.toString(),
        )
    }
}

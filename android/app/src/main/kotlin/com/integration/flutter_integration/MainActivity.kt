package com.integration.flutter_integration

import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.IOException

class MainActivity : FlutterActivity() {
    private val channel = "com.integration.flutter_integration/device"

    // Set up a MethodChannel to handle method calls from Flutter
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channel
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getDeviceInfo" -> {
                    Log.d("MainActivity", "getDeviceInfo called")
                    val deviceInfo = getDeviceInfo()
                    Log.d("MainActivity", "Device Info: $deviceInfo")
                    result.success(deviceInfo)
                }

                "getVideoInfo" -> {
                    Log.d("MainActivity", "getVideoInfo called")
                    val videoInfo = getVideoInfo()
                    Log.d("MainActivity", "Video Info: $videoInfo")
                    result.success(videoInfo)
                }

                else -> {
                    Log.d("MainActivity", "Method not implemented: ${call.method}")
                    result.notImplemented()
                }
            }
        }
    }

    /**
     * Retrieves device information using the Build class.
     * @return A map containing various device details.
     */
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

    /**
     * Retrieves video information using MediaMetadataRetriever and MediaExtractor.
     * @return A map containing various video details including duration, resolution, bitrate, and codec.
     */
    private fun getVideoInfo(): Map<String, String> {
        val retriever = MediaMetadataRetriever()

        // Locate the saved video inside the raw directory
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

        // Extract codec information
        val codec = getCodecInfo(videoUri)

        return mapOf(
            "Duration" to "${durationMinutes}m ${durationSeconds}s",
            "Resolution" to "${width}x$height",
            "Bitrate" to "$bitrate bps",
            "MIME Type" to "$mimeType",
            "Video Rotation" to "$videoRotation",
            "Codec" to codec,
            "Saved Path" to videoUri.toString(),
        )
    }

    /**
     * Retrieves the codec information for a video using MediaExtractor and MediaCodecList.
     * @param videoUri The URI of the video.
     * @return The codec name or "Unknown" if it cannot be determined.
     */
    private fun getCodecInfo(videoUri: Uri): String {
        val extractor = MediaExtractor()
        try {
            val afd = contentResolver.openAssetFileDescriptor(videoUri, "r") ?: return "Unknown"
            extractor.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                if (format.getString(MediaFormat.KEY_MIME)?.startsWith("video/") == true) {
                    val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
                    val codecName = codecList.findDecoderForFormat(format)
                    afd.close()
                    return codecName ?: "Unknown"
                }
            }
        } catch (e: IOException) {
            Log.e("MainActivity", "Error extracting codec info", e)
        } finally {
            extractor.release()
        }
        return "Unknown"
    }
}

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:developer' as developer;

import '../common/styles.dart';
import '../widgets/info_button.dart';
import '../widgets/info_list.dart';

class InfoScreen extends StatefulWidget {
  const InfoScreen({super.key});

  @override
  State<InfoScreen> createState() => _InfoScreenState();
}

class _InfoScreenState extends State<InfoScreen> {
  // Define a MethodChannel for communicating with the native platform
  static const platform =
      MethodChannel('com.integration.flutter_integration/device');

  Map<String, String> _info = {};

  /// Calls the native method to get device information.
  /// Updates the _info map with the retrieved data or an error message.
  Future<void> _getDeviceInfo() async {
    developer.log('Invoking getDeviceInfo', name: 'InfoScreen');
    Map<String, String> info;
    try {
      final result =
          await platform.invokeMethod<Map<dynamic, dynamic>>('getDeviceInfo');
      info = Map<String, String>.from(result!);
      developer.log('Device Info: $info', name: 'InfoScreen');
    } on PlatformException catch (e) {
      info = {'Error': "Failed to get device info."};
      developer.log('Error retrieving device info: ${e.message}', name: 'InfoScreen', error: e);
    }

    setState(() {
      _info = info;
    });
  }

  /// Calls the native method to get video information.
  /// Updates the _info map with the retrieved data or an error message.
  Future<void> _getVideoInfo() async {
    developer.log('Invoking getVideoInfo', name: 'InfoScreen');
    Map<String, String> info;
    try {
      final result =
          await platform.invokeMethod<Map<dynamic, dynamic>>('getVideoInfo');
      info = Map<String, String>.from(result!);
      developer.log('Video Info: $info', name: 'InfoScreen');
    } on PlatformException catch (e) {
      info = {'Error': "Failed to get video info."};
      developer.log('Error retrieving video info: ${e.message}', name: 'InfoScreen', error: e);
    }

    setState(() {
      _info = info;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Native Integration Demo'),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 20.0),
        child: Column(
          children: <Widget>[
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                InfoButton(
                  onPressed: _getDeviceInfo,
                  label: 'Get Device Info',
                ),
                const SizedBox(width: 10),
                InfoButton(
                  onPressed: _getVideoInfo,
                  label: 'Get Video Info',
                ),
              ],
            ),
            Expanded(
              child: Center(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: _info.isEmpty
                      ? const Text(
                    'Information will be displayed here',
                    style: labelTextStyle,
                    textAlign: TextAlign.center,
                  )
                      : InfoList(info: _info),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
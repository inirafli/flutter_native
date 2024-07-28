import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../common/styles.dart';
import '../widgets/info_button.dart';
import '../widgets/info_list.dart';

class InfoScreen extends StatefulWidget {
  const InfoScreen({super.key});

  @override
  State<InfoScreen> createState() => _InfoScreenState();
}

class _InfoScreenState extends State<InfoScreen> {
  static const platform =
      MethodChannel('com.integration.flutter_integration/device');

  Map<String, String> _info = {};

  Future<void> _getDeviceInfo() async {
    Map<String, String> info;
    try {
      final result =
          await platform.invokeMethod<Map<dynamic, dynamic>>('getDeviceInfo');
      info = Map<String, String>.from(result!);
    } on PlatformException catch (e) {
      info = {'Error': "Failed to get device info: '${e.message}'."};
    }

    setState(() {
      _info = info;
    });
  }

  Future<void> _getVideoInfo() async {
    Map<String, String> info;
    try {
      final result =
          await platform.invokeMethod<Map<dynamic, dynamic>>('getVideoInfo');
      info = Map<String, String>.from(result!);
    } on PlatformException catch (e) {
      info = {'Error': "Failed to get video info: '${e.message}'."};
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
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class InfoScreen extends StatefulWidget {
  const InfoScreen({super.key});

  @override
  State<InfoScreen> createState() => _InfoScreenState();
}

class _InfoScreenState extends State<InfoScreen> {
  static const platform =
      MethodChannel('com.integration.flutter_integration/device');

  String _info = 'Information will be displayed over here.';

  Future<void> _getDeviceInfo() async {
    String info;
    try {
      final String result = await platform.invokeMethod('getDeviceInfo');
      info = result;
    } on PlatformException catch (e) {
      info = "Failed to get device info: '${e.message}'.";
    }

    setState(() {
      _info = info;
    });
  }

  Future<void> _getVideoInfo() async {
    String info;
    try {
      final String result = await platform.invokeMethod('getVideoInfo');
      info = result;
    } on PlatformException catch (e) {
      info = "Failed to get video info: '${e.message}'.";
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
      body: Center(
          child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          ElevatedButton(
            onPressed: _getDeviceInfo,
            child: const Text('Get Device Info'),
          ),
          ElevatedButton(
              onPressed: _getVideoInfo, child: const Text('Get Video Info')),
          const SizedBox(height: 24.0),
          Text(_info),
        ],
      )),
    );
  }
}

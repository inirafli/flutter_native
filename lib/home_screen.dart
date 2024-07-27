import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
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
          const SizedBox(height: 24.0),
          Text(_info),
        ],
      )),
    );
  }
}

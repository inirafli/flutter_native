import 'package:flutter/material.dart';
import '../common/styles.dart';

class InfoList extends StatelessWidget {
  final Map<String, String> info;

  const InfoList({required this.info, super.key});

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: info.entries.map((entry) {
        final value = (entry.value.isEmpty) ? 'Unknown' : entry.value;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              entry.key,
              style: labelTextStyle,
            ),
            Text(
              value,
              style: valueTextStyle,
            ),
            const SizedBox(height: 12.0),
          ],
        );
      }).toList(),
    );
  }
}

import 'package:flutter/material.dart';

class InfoButton extends StatelessWidget {
  final VoidCallback onPressed;
  final String label;

  const InfoButton({required this.onPressed, required this.label, super.key});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: ElevatedButton(
        onPressed: onPressed,
        child: Text(label),
      ),
    );
  }
}

import 'package:flutter/material.dart';

final ThemeData appTheme = ThemeData(
  appBarTheme: const AppBarTheme(
    titleTextStyle: TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
    backgroundColor: Colors.blue,
    foregroundColor: Colors.white,
  ),
  elevatedButtonTheme: ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      backgroundColor: Colors.blue,
      foregroundColor: Colors.white,
      padding: const EdgeInsets.symmetric(vertical: 12.0),
      textStyle: const TextStyle(fontSize: 14.0, fontWeight: FontWeight.bold),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(6),
      ),
    ),
  ),
);

const TextStyle labelTextStyle = TextStyle(fontSize: 14.0);
const TextStyle valueTextStyle = TextStyle(fontSize: 16.0, fontWeight: FontWeight.bold);

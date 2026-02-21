#ifndef PLANTY_NANNY_H
#define PLANTY_NANNY_H

#include <Arduino.h>
#include <ESP32Servo.h>
#include <ArduinoJson.h>
#include "esp_camera.h"
#include "board_config.h"
#include <mbedtls/base64.h> // Built-in ESP32 hardware encoding

extern void sendStompSend(String destination, String jsonBody);

class PlantyNanny {
private:
  int waterServoPin = 18;
  Servo waterServo;
  int lightServoPin = 19;
  Servo lightServo;
  bool lightStatus = false;
  const unsigned long interval = 5000; // Doing something for 5 seconds
  const char* uploadLink = "http://192.168.1.89:8080/api/camera/upload";
  // camera config here
  // ultrasonic sensor here
  // soil sensor here
public:
  void cameraSetup();
  void pnSetup();
  int processCommand(String incomingCommand);
  int commandWater();
  int commandLight();
  int commandMeasure();
  int commandPicture();
  void captureImage();
};

#endif
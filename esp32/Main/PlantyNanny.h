#ifndef PLANTY_NANNY_H
#define PLANTY_NANNY_H

#include <Arduino.h>
#include <ESP32Servo.h>
#include <ArduinoJson.h>
#include "esp32_camera.h"
#include "board_config.h"

extern void sendStompSend(String destination, String jsonBody);

class PlantyNanny {
private:
  int waterServoPin = 18;
  Servo waterServo;
  int lightServoPin = 19;
  Servo lightServo;
  bool lightStatus = false;
  const unsigned long interval = 5000; // Doing something for 5 seconds
  // camera config here
  // ultrasonic sensor here
  // soil sensor here
public:
  void pnSetup();
  void cameraSetup();
  int processCommand(String incomingCommand);
  int commandWater();
  int commandLight();
  int commandMeasure();
  int commandPicture();
  unsigned char* captureImage();
};

#endif
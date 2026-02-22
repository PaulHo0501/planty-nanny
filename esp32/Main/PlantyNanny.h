#ifndef PLANTY_NANNY_H
#define PLANTY_NANNY_H

#include <Arduino.h>
#include <ESP32Servo.h>
#include <ArduinoJson.h>
#include <Adafruit_NeoPixel.h>
#include "esp_camera.h"
#include "board_config.h"

extern void sendStompSend(String destination, String jsonBody);

class PlantyNanny {
private:
  // led
  int lightPin = 13;
  int ledCount = 8;
  int brightness = 50;
  bool lightStatus = false;
  Adafruit_NeoPixel strip;

  const unsigned long interval = 5000; // Doing something for 5 seconds
  const char* uploadLink = "http://192.168.1.89:8080/api/camera/upload";
  const char* getCurrentLightStatusLink = "http://192.168.1.89:8080/api/tree/light-status";
  // camera config here
  // ultrasonic sensor here
  // soil sensor here
public:
  PlantyNanny();
  void cameraSetup();
  void pnSetup();
  int processCommand(String incomingCommand);
  int commandWater();
  int commandLight(bool status);
  int commandMeasure();
  int commandPicture();
  void captureImage();
  void getCurrentLightStatus();
};

#endif
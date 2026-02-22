#ifndef PLANTY_NANNY_H
#define PLANTY_NANNY_H

#include <Arduino.h>
#include <ESP32Servo.h>
#include <ArduinoJson.h>
#include <Adafruit_NeoPixel.h>
#include "esp_camera.h"
#include "board_config.h"
#include <Wire.h>
#include "Adafruit_seesaw.h"

extern void sendStompSend(String destination, String jsonBody);

class PlantyNanny {
private:
  // led
  int lightPin = 13;
  int ledCount = 8;
  int brightness = 50;
  bool lightStatus = false;
  Adafruit_NeoPixel strip;

  // humidity sensor
  int soilSDA = 14;
  int soilSCL = 15;
  const int minimumCap = 300;
  const int maximumCap = 800;
  Adafruit_seesaw ss;

  // ultrasonic sensor
  int triggerPin = 2;
  int echoPin = 3;
  const int tank_empty_cm = 17;
  const int tank_full_cm = 2;

  // water pump


  const unsigned long interval = 5000; // Doing something for 5 seconds
  const char* uploadLink = "http://192.168.1.89:8080/api/camera/upload";
  const char* getCurrentLightStatusLink = "http://192.168.1.89:8080/api/tree/light-status";

public:
  PlantyNanny();
  void setupUltrasonic();
  int getWaterLevelPercentage();
  void cameraSetup();
  void pnSetup();
  int processCommand(String incomingCommand);
  int commandWater();
  int commandLight(bool status);
  int commandMeasure();
  int commandPicture();
  void captureImage();
  void getCurrentLightStatus();
  int getHumidityPercentage();
};

#endif
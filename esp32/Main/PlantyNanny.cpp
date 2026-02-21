#include "PlantyNanny.h"

void PlantyNanny::pnSetup() {
  waterServo.attach(waterServoPin);
  lightServo.attach(lightServoPin);
  Serial.println("[PN] Done Setup");
}

int PlantyNanny::processCommand(String incomingCommand) {
  JsonDocument doc;
  deserializeJson(doc, incomingCommand);
  if (strcmp(doc["command"], "WATER")) {
    return commandWater();
  }
  else if (strcmp(doc["command"], "LIGHT")) {
    return commandLight();
  }
  return 0;
}

int PlantyNanny::commandWater() {
  unsigned long last = millis();
  waterServo.write(90);
  unsigned long now = millis();
  while (now - last <= interval) {
    now = millis();
  }
  waterServo.write(0);
  return 0;
}

int PlantyNanny::commandLight() {
}

int PlantyNanny::commandMeasure() {
  return 0;
}

int PlantyNanny::commandPicture() {
  return 0;
}
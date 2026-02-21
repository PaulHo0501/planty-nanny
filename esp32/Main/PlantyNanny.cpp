#include "PlantyNanny.h"

void PlantyNanny::pnSetup() {
  waterServo.attach(waterServoPin);
  cameraSetup();
  Serial.println("[PN] Done Camera Setup")
  Serial.println("[PN] Done Setup");
}

int PlantyNanny::processCommand(String incomingCommand) {
  JsonDocument doc;
  deserializeJson(doc, incomingCommand);
  if (strcmp(doc["command"], "WATER") == 0) {
    return commandWater();
  }
  else if (strcmp(doc["command"], "LIGHT") == 0) {
    return commandLight();
  }
  else if (strcmp(doc["command"], "PICTURE") == 0) {
    return commandPicture();
  }
  return 0;
}

unsigned char* captureImage() {
  Serial.println("Capturing image...");
    camera_fb_t * fb = esp_camera_fb_get();
    if (!fb) {
        Serial.println("Camera capture failed");
        return;
    }

    size_t outputLength;
    size_t base64BufferSize = ((fb->len + 2) / 3) * 4 + 1; 
    unsigned char * base64Buffer = (unsigned char *) malloc(base64BufferSize);
    
    if (base64Buffer == NULL) {
        Serial.println("Failed to allocate Base64 buffer");
        esp_camera_fb_return(fb); 
        return;
    }

    int err = mbedtls_base64_encode(base64Buffer, base64BufferSize, &outputLength, fb->buf, fb->len);
    
    if (err == 0) {
        Serial.println("Image encoded. Sending to server...");
        String jsonPayload = "{\"type\":\"IMAGE\", \"data\":\"";
        jsonPayload += (char *)base64Buffer;
        jsonPayload += "\"}";

        // This works because we used 'extern' in the .h file!
        sendStompSend("/app/camera/upload", jsonPayload);
    }

    free(base64Buffer);
    esp_camera_fb_return(fb);

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
  captureImage();
  return 0;
}
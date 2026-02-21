#include "PlantyNanny.h"
#include <HTTPClient.h>
#include <WiFi.h>

void PlantyNanny::cameraSetup() {
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sccb_sda = SIOD_GPIO_NUM;
  config.pin_sccb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 5000000;
  config.frame_size = FRAMESIZE_UXGA;
  config.pixel_format = PIXFORMAT_JPEG;
  // config.grab_mode = CAMERA_GRAB_LATEST;
  config.fb_location = CAMERA_FB_IN_PSRAM;
  config.jpeg_quality = 5;
  config.fb_count = 1;
  // camera init
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
      Serial.printf("Camera init failed with error 0x%x", err);
      return;
  }
  sensor_t * s = esp_camera_sensor_get();
  if (s != NULL) {
    s->set_contrast(s, 1);       // Bump contrast slightly (0 to 2)
    s->set_saturation(s, 1);     // Make colors pop slightly (0 to 2)
    s->set_whitebal(s, 1);       // Enable Auto White Balance
    s->set_awb_gain(s, 1);       // Enable Auto White Balance Gain
    s->set_exposure_ctrl(s, 1);  // Enable Auto Exposure
    s->set_gain_ctrl(s, 1);      // Enable Auto Gain (helps in low light)
    
    // Critical Fixes for OV2640 Sensors
    s->set_bpc(s, 1);            // Enable Black Pixel Correction (removes dead pixels)
    s->set_wpc(s, 1);            // Enable White Pixel Correction (removes hot pixels)
    s->set_lenc(s, 1);           // Enable Lens Correction (fixes dark vignetting in corners)
  }
  Serial.println("[PN] Done Camera Setup");
  return;
}

void PlantyNanny::pnSetup() {
  waterServo.attach(waterServoPin);
  lightServo.attach(lightServoPin);
  cameraSetup();
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
  unsigned long last = millis();
  lightServo.write(90);
  unsigned long now = millis();
  while (now - last <= interval) {
    now = millis();
  }
  lightServo.write(0);
  return 0;
}

int PlantyNanny::commandMeasure() {
  return 0;
}

void PlantyNanny::captureImage() {
  Serial.println("Capturing image...");

  camera_fb_t * stale_fb = esp_camera_fb_get();
  if (stale_fb) {
      esp_camera_fb_return(stale_fb); 
  }

  camera_fb_t * fb = esp_camera_fb_get();
  if (!fb) {
      Serial.println("Camera capture failed");
      return;
  }
  WiFiClient client;
  HTTPClient http;
  http.begin(client, "http://192.168.1.74:8080/api/camera/upload");

  http.addHeader("Content-Type", "image/jpeg");

  int httpResponseCode = http.POST(fb->buf, fb->len);

  if (httpResponseCode > 0) {
      Serial.printf("HTTP Response code: %d\n", httpResponseCode);
  } else {
      Serial.printf("HTTP POST Error: %s\n", http.errorToString(httpResponseCode).c_str());
  }

  http.end();
  esp_camera_fb_return(fb); 
  Serial.println("Capture and upload cycle complete.");

}

int PlantyNanny::commandPicture() {
  Serial.println("Capturing image...");
  captureImage();
  return 0;
}
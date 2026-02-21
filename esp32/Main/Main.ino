#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <WebSocketsClient.h>

#include "PlantyNanny.h"
#include "credential.h"

WiFiMulti wifiMulti;
WebSocketsClient webSocket;
PlantyNanny pn;

bool stompConnected = false;

void sendStompConnect() {
    String frame = "CONNECT\n";
    frame += "accept-version:1.2,1.1,1.0\n";
    frame += "heart-beat:10000,10000\n";
    frame += "\n"; 

    webSocket.sendTXT((uint8_t *)frame.c_str(), frame.length() + 1);
}

void sendStompSubscribe(String id, String destination) {
    String frame = "SUBSCRIBE\n";
    frame += "id:" + id + "\n";
    frame += "destination:" + destination + "\n";
    frame += "ack:auto\n";
    frame += "\n";
    
    webSocket.sendTXT((uint8_t *)frame.c_str(), frame.length() + 1);
}

void sendStompSend(String destination, String jsonBody) {
    String frame = "SEND\n";
    frame += "destination:" + destination + "\n";
    frame += "content-type:application/json\n";
    frame += "\n";
    frame += jsonBody;
    frame += "\n";
    
    webSocket.sendTXT((uint8_t *)frame.c_str(), frame.length() + 1);
}

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {
    String text = (char*)payload;

    switch(type) {
        case WStype_DISCONNECTED:
            Serial.println("[WS] Disconnected!");
            stompConnected = false;
            break;

        case WStype_CONNECTED:
            Serial.println("[WS] Connected! Sending STOMP Handshake...");
            sendStompConnect();
            break;

        case WStype_TEXT:
            if (text.startsWith("CONNECTED")) {
                Serial.println("[STOMP] Connected successfully!");
                stompConnected = true;
                sendStompSubscribe("0", "/topic/commands");
            }
            else if (text.startsWith("MESSAGE")) {
                int bodyIndex = text.indexOf("\n\n");
                if (bodyIndex > 0) {
                    String body = text.substring(bodyIndex + 2);
                    body.trim(); 
                    Serial.print("[STOMP] Message Received: ");
                    Serial.println(body);
                    pn.processCommand(body);
                    // TODO: Parse 'body' with ArduinoJson here
                }
            }
            break;
    }
}

void setup() {
  Serial.begin(115200);

  pn.pnSetup();

  for (uint8_t t = 4; t > 0; t--) {
    Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
    delay(1000);
  }

  wifiMulti.addAP(WIFI_SSID, WIFI_Password);
  Serial.println("Local IP:");
  Serial.println(WiFi.localIP());

  webSocket.begin(WS_HOST, WS_PORT, "/gs-guide-websocket"); 
  webSocket.onEvent(webSocketEvent);
  webSocket.setReconnectInterval(5000);
}

void loop() {
  webSocket.loop();
}
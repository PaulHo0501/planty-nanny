#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <WebSocketsClient.h>

#include "credential.h"

#include "PlantyNanny.h"

WiFiMulti wifiMulti;
WebSocketsClient webSocket;
PlantyNanny pn;

// Variables for non-blocking timer
unsigned long lastSendTime = 0;
const unsigned long sendInterval = 5000; // Send every 5 seconds
bool stompConnected = false;

// --- STOMP FUNCTIONS ---

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

// --- WEBSOCKET EVENT HANDLER ---

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
            // 1. Handle STOMP CONNECTED
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
                }
            }
            break;
    }
}

// --- SETUP & LOOP ---
void setup() {
    Serial.begin(115200);
    pn.pnSetup();
    delay(5000);
    // WiFi Setup
    wifiMulti.addAP(WIFI_SSID, WIFI_Password);
    WiFi.setSleep(false);
    Serial.print("Connecting to WiFi");
    while(wifiMulti.run() != WL_CONNECTED) {
        Serial.print(".");
        delay(100);
    }
    Serial.println("\nWiFi connected");
    Serial.print("IP: "); Serial.println(WiFi.localIP());
    Serial.println("Camera initialized successfully!");

    // WebSocket Setup
    // Note: Remove the trailing slash in the URL if your server config is strict
    webSocket.begin(WS_HOST, WS_PORT, "/gs-guide-websocket"); 
    webSocket.onEvent(webSocketEvent);
    webSocket.setReconnectInterval(5000);

    pn.getCurrentLightStatus();
}

void loop() {
    // 1. Always keep the socket alive
    webSocket.loop();
}
# Planty Nanny

Planty Nanny is an integrated IoT and Computer Vision ecosystem functioned as an autonomous greenhouse climate management. 

It syncronizes physical sensor, a webserver, and an Android controller to maintain optimal growth for the given plant with **near-zero

human interaction**. 


However, Planty Nanny is **NOT** just a static automation system.
  

The moment a plant is introduced to the physical base, Planty Nanny will be able to identitfy its species and reconfigures itself into

the perfect home for that lucky plant.


Not to mention, Planty Nanny also acts as witty doctor that scans and checks the plant's health daily. If any sign of sickness shows,

it will send a diagnosis straight to your phon, ensuring your plant stays healthy and thriving.


## Gallery

<img width="300" height="808" alt="Start Screen" src="https://github.com/user-attachments/assets/32affe45-d586-4978-bac5-a662fe3bfb8b" />
<img width="300" height="808" alt="Main Screen - No Plant" src="https://github.com/user-attachments/assets/a6fb80e1-b297-4ea1-8a36-ce6846c7624d" />
<img width="300" height="808" alt="Add Plant Screen1" src="https://github.com/user-attachments/assets/f81b97b3-eb4f-4fda-9dfd-c750cd9f3ad7" />
<img width="300" height="808" alt="Add Plant Screen2" src="https://github.com/user-attachments/assets/8231716c-c1fa-4eb4-ae93-10616d47ae3a" />
<img width="300" height="808" alt="Main Screen - Plant Identified" src="https://github.com/user-attachments/assets/9379ef63-a2b7-43fc-8e19-44059abad024" />
<img width="300" height="808" alt="Main Screen - Soil Humidity" src="https://github.com/user-attachments/assets/3f62a2e0-c248-484f-b101-01f064650201" />
<img width="300" height="808" alt="Main Screen - High Tank Water Level" src="https://github.com/user-attachments/assets/21f75e80-b753-4e48-9e5d-069bc232c0ff" />
<img width="300" height="808" alt="Main Screen - Light Status - Light On" src="https://github.com/user-attachments/assets/9298ea3e-22a7-4651-a430-82c22f16239c" />
<img width="300" height="808" alt="Main Screen - Health Condition Level" src="https://github.com/user-attachments/assets/0dc0bffe-ad4f-44db-ace1-3a85f7ded1b2" />
<img width="450" height="808" alt="Picture 2" src="https://github.com/user-attachments/assets/8eee27a2-a6f3-416d-878f-37821a5a95a5" />
<img width="450" height="808" alt="Picture 2" src="https://github.com/user-attachments/assets/4e1b8929-5026-4103-b78f-90115c117787" />
<img width="900" height="808" alt="Picture 3" src="https://github.com/user-attachments/assets/d04309ea-b850-474f-92cc-201f86260b49" />

## Technical Info

#### 1. Client Layer
   - Language: Kotlin
   - Tech stack: Jetpack Compose
   - Functionality: Acts as the main UI and uses STOMP WebSockets to maintain a live synchronization with the server and sensors
#### 2. Service Layer
   - Language: Java
   - Tech Stack: Spring Boot
   - Functionality:
     - Manages data flow between IoT sensors and the AI engine
     - Manages AWS S3 integrations for image storage
     - Decides automation logic based on incoming telemetry and species-specific requirements 
#### 3. Data Layer
   - Language: SQL
   - Tech Stack: MySQL, AWS S3, Docker
   - Functionality: Stores plant information and picutres taken from the camera 
#### 4. Hardware Layer
   - Language: Arduino, C++
   - Hardware: ESP32, ESP32Camera, Soil Humidity sensor, Ultrasonic sensor, Water pump, NeoPixel LED
   - Functionality:
      - ESP32: Manages Wi-Fi connectivity to the Spring Boot server and handles the timing for sensor polling and actuator triggers
      - ESP32-Camera: Captures images of the plant. This image is then transmitted to the AI engine for species identification upon placement or used for daily health diagnostics
      - Soil Humidity Sensor: Provides real-time moisture data from the base and will trigger the water pump if current moisture falls below the allowed range
      - Ultrasonic Sensor: Measures the water level in the external tank and alerts the Android App when a refill is required
      - Water Pump:  When activated, the pump delivers water based on the plant's needs
      - NeoPixel LED: Provides full-spectrum lighting for the plant. This is triggered automatically at 7 AM sharp and will stop after the plant has enough sunlight for the day

##  Getting started
1. Clone this repository
2. Make sure you have Docker installed on your machine
3. Setup AWS S3 service using your own account
4. Obtain your personal Gemini API key (**DO NOT PUSH THIS TO GITHUB**)
5. Build and run the backend code (Make sure to include Gemini API Key and AWS S3 key in your environment variable before running)
6. Upload the hardware code to your hardware
7. Build and run the frontend code
8. Enjoy your own cool little greenhouse!

## Extra Resources
- [Set up your Gemini API key](https://ai.google.dev/gemini-api/docs/api-key)
- [Set up your AWS S3 service](https://aws.amazon.com/s3/)

## Team Members
If you find Planty Nanny as exciting as we do, we’d love to connect!
- [Duc Ho](https://www.linkedin.com/in/paulho0501/)
- [Thea Nguyen](www.linkedin.com/in/thea-ng)


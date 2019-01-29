#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "<YOUR_ID>.firebaseio.com"
#define WIFI_SSID "<YOUR_SSID>"
#define WIFI_PASSWORD "<YOUR_PASSWORD"
#define LED_PIN 5
 

void setup() {
    Serial.begin(9600);

    pinMode(LED_PIN, OUTPUT);

    // connect to wifi.
    WiFi.disconnect();
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("connecting");
    while (WiFi.status() != WL_CONNECTED) {
        Serial.print(".");
        delay(500);
    }
    Serial.println();
    Serial.print("connected: ");
    Serial.println(WiFi.localIP());
  
    Firebase.begin(FIREBASE_HOST);
}


void loop() {
    // get value 
    Serial.print("led: ");
    Serial.println(Firebase.getInt("light/on"));
    digitalWrite(LED_PIN, Firebase.getInt("light/on"));
    delay(1000);
}

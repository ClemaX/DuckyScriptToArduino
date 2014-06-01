DuckyScriptToArduino
====================

Convert Duckyscript to Arduino C Code

This project got inspierd by: http://www.adebenham.com/2012/05/usb-rubber-ducky-scripts-on-arduinoleostick/
and also http://hakshop.myshopify.com/products/usb-rubber-ducky
but as i only had an Arduino Uno Rev 2 i could not use the Keyboard classes to use the arduino as a HID.

Later i found a DFU-Hex to flash the Atmega8U2 and add the HID functionallity through the Serial.write to do a keyboard press.
Here is the blog post i found: http://mitchtech.net/arduino-usb-hid-keyboard/

Here is the source and hex file: http://hunt.net.nz/users/darran/weblog/b3029/Arduino_UNO_Keyboard_HID_version_03.html

Note that this is still in its early stages so its limited to swedish keyboard layout and not all functions have been translated yet.

/*
* Name:            DuckyScriptToArduino
* Author:          Marcus Orwen
* Copyright:       Copyright (c) 2013, Marcus Orwen
* License:         GNU GPL v3
* License file:    LICENSE.md
* You are NOT authorized to change or remove this comment box.
*/

package duckyscripttoarduino;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert almost any duckky script to arduino code so we can use this on
 * Arduino UNO Rev 2 or above (or even Rev 1 cannot test though).
 *
 * TODO:
 * Finish translating keyinputs to Swe/Sv
 * Add localisation file (for other keyboards/languages)
 * 
 * Needs implamentation:
 * KEY_MENU (Needs testing)
 * KEY_DOWNARROW (Needs testing)
 * KEY_UPARROW (Needs testing)
 * KEY_LEFTARROW (Needs testing)
 * KEY_RIGHTARROW (Needs testing)
 * KEY_CAPS (Needs testing)
 * KEY_PAUSE (Needs testing)
 * KEY_DELETE (Needs testing)
 * KEY_END (Needs testing)
 * KEY_ESC (Needs testing)
 * KEY_HOME (Needs testing)
 * KEY_INSERT (Needs testing)
 * KEY_NUMLOCK (Needs testing)
 * KEY_PAGEUP (Needs testing)
 * KEY_PAGEDOWN (Needs testing)
 * KEY_PRINTSCREEN (Needs testing)
 * KEY_SCROLLLOCK (Needs testing)
 * KEY_TAB (Needs testing)
 * DEFAULT_DELAY
 * 
 * @author Marcus
 */
public class DuckyScriptToArduino {

    /**
     * This will convert most duckyscript codes to arduino C code
     * 
     * @param args 
     * -i  input file
     * -o  output file
     * -d  use debug mode
     * --help or -h for displaying commands
     */
    public static void main(String[] args) {
        //get instance of this class
        DuckyScriptToArduino arduino = new DuckyScriptToArduino();
        FileHandler fileHandler = new FileHandler();
        String code;
        String debugOutput;
        String inputFile = null;
        String outputFile = null;
        String debugFile = null;
        boolean debug = false;
        boolean argErr = false;
        
        String helpStr = "@Author: USB Rubber Ducky and Original Encoder for DuckyScript created by Darren Kitchen.\n"
                + "@Author: Arduino (Uno) Duckyscript converter created by Marcus Orwén\n\n"
                + "usage: arduinoduckencode -i [file ..]\t\t\tconvert specified file\n"
                + "   or: arduinoduckencode -i [file ..] -o [file ..]\tconvert to specified file\n"
                + "\nArguments:\n"
                + "   -i [file ..] \t\tInput File\n"
                + "   -o [file ..] \t\tOutput File\n"
                + "   -d           \t\tToggle Debug Mode\n"
                + "   -do [file ..]\t\tToggle Debug File Output Mode\n";
        
        //no arguments given, show help and exit
        if(args.length == 0) {
            System.out.println(helpStr);
            System.exit(0);
        }
        
        //Handle args (parameters)
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--help") || args[i].equals("-h")){
                System.out.println(helpStr);
            }else if(args[i].equals("-o")) {
                outputFile = args[++i];
            }else if(args[i].equals("-i")) {
                inputFile = args[++i];
            }else if(args[i].equals("-d")){
                debug = true;
            }else if(args[i].equals("-do")){
                debug = true;//if not set
                debugFile = args[++i];
            }else{
                System.out.println(helpStr);
                argErr = true;
                break;
            }
        }
        
        //no arguments given exit
        if(argErr) {
            System.exit(0);
        }
        
        if(inputFile == null){
            debugOutput = "Error no input file!";
        }else{
            debugOutput = "loading file: " + inputFile + "\n";
        }
        String[] inputArr = fileHandler.load(inputFile);
        
        debugOutput = debugOutput + "Loaded file!\n";
        
        debugOutput = debugOutput + "Converting...\n";
        
        //create base (SWE keyboard info etc)
        code = "uint8_t buf[8] = {0};\n"
                + "\n"
                + "void sendKey(byte key, byte key2){\n"
                + "  buf[0] = key2;\n"
                + "  buf[2] = key;\n"
                + "  Serial.write(buf, 8);\n"
                + "  releaseKey();\n"
                + "}\n"
                + "\n"
                + "void releaseKey(){\n"
                + "  buf[0] = 0;\n"
                + "  buf[2] = 0;\n"
                + "  Serial.write(buf, 8);\n"
                + "  delay(30); // we can probably go lower but this is as low as i have tested\n"
                + "}\n"
                + "\n"
                + "void execCommand(String command){\n"
                + "  uint8_t temp_buf[8] = {0};\n"
                + "  char buffer[1000];\n"
                + "  command.toCharArray(buffer, command.length());//convert to char array\n"
                + "\n"
                + "  for(int i = 0; i <= command.length(); i++){\n"
                + "    if((int) buffer[i] >= 65 && (int) buffer[i] <= 90){//Uppercase characters\n"
                + "      temp_buf[0] = 0x02; //KEY_LEFTSHIFT\n"
                + "    } else {\n"
                + "      switch(buffer[i]){\n"
                + "        case '%':\n"
                + "        case ':':\n"
                + "        case '(':\n"
                + "        case ')':\n"
                + "        case '/':\n"
                + "        case '=':\n"
                + "        case '\"':\n"
                + "        case '_':\n"
                + "        case '!':\n"
                + "        case '#':\n"
                + "        case '¤':\n"
                + "        case '&':\n"
                + "        case '*':\n"
                + "        case '>':\n"
                + "        case ';':\n"
                + "        case '?':\n"
                + "          temp_buf[0] = 0x02; //KEY_LEFTSHIFT\n"
                + "          break;\n"
                + "        case '|':\n"
                + "        case '@':\n"
                + "        case '}':\n"
                + "        case '£':\n"
                + "        case '$':\n"
                + "        case '€':\n"
                + "        case '{':\n"
                + "        case '[':\n"
                + "        case ']':\n"
                + "        case '\\\\':\n" //this will result in \\ and then later on \
                + "          temp_buf[0] = 0x40; //KEY_RIGHTALT\n"
                + "          break;\n"
                + "      }\n"
                + "    }\n"
                + "    switch(buffer[i]){\n"
                + "      case ' ':\n"
                + "        temp_buf[2] = 0x2C;\n"
                + "        break;\n"
                + "      case '-':\n"
                + "      case '_':\n"
                + "        temp_buf[2] = 0x38;\n"
                + "        break;\n"
                + "      case '^':\n"
                + "      //case '~':\n"
                + "      //case '¨':\n"
                + "        sendKey(0x30, 0x02);// KEY_TOPDOT //KEY_LEFTSHIFT\n"
                + "        temp_buf[2] = 0x2C;//send a space to enter the previous ^\n"
                + "        break;\n"
                + "      case '=':\n"
                + "      case '}':\n"
                + "      case '0':\n"
                + "        temp_buf[2] = 0x27;\n"
                + "        break;\n"
                + "      case '!':\n"
                + "      case '1':\n"
                + "        temp_buf[2] = 0x1E;\n"
                + "        break;\n"
                + "      case '\"':\n"
                + "      case '@':\n"
                + "      case '2':\n"
                + "        temp_buf[2] = 0x1F;\n"
                + "        break;\n"
                + "      case '#':\n"
                + "      case '£':\n"
                + "      case '3':\n"
                + "        temp_buf[2] = 0x20;\n"
                + "        break;\n"
                + "      case '¤':\n"
                + "      case '$':\n"
                + "      case '4':\n"
                + "        temp_buf[2] = 0x21;\n"
                + "        break;\n"
                + "      case '%':\n"
                + "      case '€':\n"
                + "      case '5':\n"
                + "        temp_buf[2] = 0x22;\n"
                + "        break;\n"
                + "      case '&':\n"
                + "      case '6':\n"
                + "        temp_buf[2] = 0x23;\n"
                + "        break;\n"
                + "      case '/':\n"
                + "      case '{':\n"
                + "      case '7':\n"
                + "        temp_buf[2] = 0x24;\n"
                + "        break;\n"
                + "      case '(':\n"
                + "      case '[':\n"
                + "      case '8':\n"
                + "        temp_buf[2] = 0x25;\n"
                + "        break;\n"
                + "      case ')':\n"
                + "      case ']':\n"
                + "      case '9':\n"
                + "        temp_buf[2] = 0x26;\n"
                + "        break;\n"
                + "      case '\\\'':\n"
                + "      case '*':\n"
                + "        temp_buf[2] = 0x31;\n"
                + "        break;\n"
                + "      case '|':\n"
                + "      case '<':\n"
                + "      case '>':\n"
                + "        temp_buf[2] = 0x64;\n"
                + "        break;\n"
                + "      case ':':\n"
                + "      case '.':\n"
                + "        temp_buf[2] = 0x37;\n"
                + "        break;\n"
                + "      case ',':\n"
                + "      case ';':\n"
                + "        temp_buf[2] = 0x36;\n"
                + "        break;\n"
                + "      case '?':\n"
                + "      case '+':\n"
                + "      case '\\\\':\n"
                + "        temp_buf[2] = 0x2D;\n"
                + "        break;\n"
                + "      case 'a':\n"
                + "      case 'A':\n"
                + "        temp_buf[2] = 0x04;\n"
                + "        break;\n"
                + "      case 'b':\n"
                + "      case 'B':\n"
                + "        temp_buf[2] = 0x05;\n"
                + "        break;\n"
                + "      case 'c':\n"
                + "      case 'C':\n"
                + "        temp_buf[2] = 0x06;\n"
                + "        break;\n"
                + "      case 'd':\n"
                + "      case 'D':\n"
                + "        temp_buf[2] = 0x07;\n"
                + "        break;\n"
                + "      case 'e':\n"
                + "      case 'E':\n"
                + "        temp_buf[2] = 0x08;\n"
                + "        break;\n"
                + "      case 'f':\n"
                + "      case 'F':\n"
                + "        temp_buf[2] = 0x09;\n"
                + "        break;\n"
                + "      case 'g':\n"
                + "      case 'G':\n"
                + "        temp_buf[2] = 0x0A;\n"
                + "        break;\n"
                + "      case 'h':\n"
                + "      case 'H':\n"
                + "        temp_buf[2] = 0x0B;\n"
                + "        break;\n"
                + "      case 'i':\n"
                + "      case 'I':\n"
                + "        temp_buf[2] = 0x0C;\n"
                + "        break;\n"
                + "      case 'j':\n"
                + "      case 'J':\n"
                + "        temp_buf[2] = 0x0D;\n"
                + "        break;\n"
                + "      case 'k':\n"
                + "      case 'K':\n"
                + "        temp_buf[2] = 0x0E;\n"
                + "        break;\n"
                + "      case 'l':\n"
                + "      case 'L':\n"
                + "        temp_buf[2] = 0x0F;\n"
                + "        break;\n"
                + "      case 'm':\n"
                + "      case 'M':\n"
                + "        temp_buf[2] = 0x10;\n"
                + "        break;\n"
                + "      case 'n':\n"
                + "      case 'N':\n"
                + "        temp_buf[2] = 0x11;\n"
                + "        break;\n"
                + "      case 'o':\n"
                + "      case 'O':\n"
                + "        temp_buf[2] = 0x12;\n"
                + "        break;\n"
                + "      case 'p':\n"
                + "      case 'P':\n"
                + "        temp_buf[2] = 0x13;\n"
                + "        break;\n"
                + "      case 'q':\n"
                + "      case 'Q':\n"
                + "        temp_buf[2] = 0x14;\n"
                + "        break;\n"
                + "      case 'r':\n"
                + "      case 'R':\n"
                + "        temp_buf[2] = 0x15;\n"
                + "        break;\n"
                + "      case 's':\n"
                + "      case 'S':\n"
                + "        temp_buf[2] = 0x16;\n"
                + "        break;\n"
                + "      case 't':\n"
                + "      case 'T':\n"
                + "        temp_buf[2] = 0x17;\n"
                + "        break;\n"
                + "      case 'u':\n"
                + "      case 'U':\n"
                + "        temp_buf[2] = 0x18;\n"
                + "        break;\n"
                + "      case 'v':\n"
                + "      case 'V':\n"
                + "        temp_buf[2] = 0x19;\n"
                + "        break;\n"
                + "      case 'w':\n"
                + "      case 'W':\n"
                + "        temp_buf[2] = 0x1A;\n"
                + "        break;\n"
                + "      case 'x':\n"
                + "      case 'X':\n"
                + "        temp_buf[2] = 0x1B;\n"
                + "        break;\n"
                + "      case 'y':\n"
                + "      case 'Y':\n"
                + "        temp_buf[2] = 0x1C;\n"
                + "        break;\n"
                + "      case 'z':\n"
                + "      case 'Z':\n"
                + "        temp_buf[2] = 0x1D;\n"
                + "        break;\n"
                + "    }\n"
                + "    sendKey(temp_buf[2], temp_buf[0]);//send keystroke of current key in for loop\n"
                + "    temp_buf[0] = 0;//reset to 0\n"
                + "    temp_buf[2] = 0;//reset to 0\n"
                + "  }\n"
                + "}\n"
                + "\n"
                + "void loop(){\n"
                + "}\n"
                + "\n"
                + "void setup(){\n"
                + "  Serial.begin(9600);\n"
                + "  delay(2000);\n";

        code = arduino.convertLoop(code, inputArr, debug);
        
        code = code
                + "  Serial.end();\n"
                + "}";
        
        //debug output
        if(debug){
            debugOutput = debugOutput + code + "\n";
        }
        debugOutput = debugOutput + "Convertion finished!\n";
        debugOutput = debugOutput + "Saving...\n";
        
        //save file, if no output file specified use payload.ino
        debugOutput = debugOutput + fileHandler.save(code, (outputFile == null) ? "payload.ino" : outputFile);
        
        System.out.println(debugOutput);
        if(debug){
            fileHandler.save(debugOutput, (debugFile == null) ? "debug.txt" : debugFile);
        }
    }
    
    /**
     * ConvertToHex
     * @param key need to be in format KEY_* where * is the key, ex KEY_A
     * @return keyboard hex of the key you want, ex KEY_A = 0x04
     */
    private String convertToHex(String key){
        Map<String, String> map = new HashMap<>();
        //      Name     Hex               modifiers: none  shift  alt
        map.put("KEY_A", "0x04"); // A key on keyboard", 'a' or 'A' or ''
        map.put("KEY_B", "0x05");
        map.put("KEY_C", "0x06");
        map.put("KEY_D", "0x07");
        map.put("KEY_E", "0x08");
        map.put("KEY_F", "0x09");
        map.put("KEY_G", "0x0A");
        map.put("KEY_H", "0x0B");
        map.put("KEY_I", "0x0C");
        map.put("KEY_J", "0x0D");
        map.put("KEY_K", "0x0E");
        map.put("KEY_L", "0x0F");
        map.put("KEY_M", "0x10");
        map.put("KEY_N", "0x11");
        map.put("KEY_O", "0x12");
        map.put("KEY_P", "0x13");
        map.put("KEY_Q", "0x14");
        map.put("KEY_R", "0x15");
        map.put("KEY_S", "0x16");
        map.put("KEY_T", "0x17");
        map.put("KEY_U", "0x18");
        map.put("KEY_V", "0x19");
        map.put("KEY_W", "0x1A");
        map.put("KEY_X", "0x1B");
        map.put("KEY_Y", "0x1C");
        map.put("KEY_Z", "0x1D");
        map.put("KEY_1", "0x1E"); // 1 key on keyboard", '1' or '!'
        map.put("KEY_2", "0x1F"); // 2 key on keyboard", '2' or 'double-qoutes' or '@'
        map.put("KEY_3", "0x20"); // 3 key on keyboard", '3' or '#' or '�'
        map.put("KEY_4", "0x21"); // 4 key on keyboard", '4' or '�' or '$'
        map.put("KEY_5", "0x22"); // 5 key on keyboard", '5' or '%' or '�'
        map.put("KEY_6", "0x23"); // 6 key on keyboard", '6' or '&'
        map.put("KEY_7", "0x24"); // 7 key on keyboard", '7' or '/' or '{'
        map.put("KEY_8", "0x25"); // 8 key on keyboard", '8' or '(' or '['
        map.put("KEY_9", "0x26"); // 9 key on keyboard", '9' or ')' or ']'
        map.put("KEY_0", "0x27"); // 0 key on keyboard", '0' or '=' or '}'
        map.put("KEY_ENTER", "0x28"); // Keyboard Enter key", different from Keypad enter
        map.put("KEY_ESC", "0x29"); // Escape
        map.put("KEY_BACKSPACE", "0x2A"); // Backspace
        map.put("KEY_TAB", "0x2B"); // Tab (TODO: Test)
        map.put("KEY_SPACE", "0x2C"); // Spacebar
        map.put("KEY_BACKSLASH", "0x2D"); // Plus key", 'backslash' or '+' or '?' (TODO: Test)
        map.put("KEY_EQUAL", "0x2E"); // Equals key", '=' or '+' (TODO: Test)
        map.put("KEY_LEFTBRACE", "0x2F"); // Left brace", '[' or '{' (TODO: Test)
        map.put("KEY_TOPDOT", "0x30"); // Right brace", '�' or '^' or '~'
        map.put("KEY_APOSTROPHE", "0x31"); // Backslash key", ''' or '*'
        map.put("KEY_NONUSHASH", "0x32"); // Non-US '#' and '~' (TODO: Test)
        map.put("KEY_SEMICOLON", "0x33"); // Semicolon key", ';' or ':' (TODO: Test)
        map.put("KEY_APOSTROPHE2", "0x34"); // Apostrophe key", ''' or double-quotes (TODO: Test)
        map.put("KEY_GRAVE", "0x35"); // Grave accent key", '' or '~' (TODO: Test)
        map.put("KEY_COMMA", "0x36"); // Comma key, '",' or ';'
        map.put("KEY_DOT", "0x37"); // Dot key", '.' or ':'
        map.put("KEY_MINUS", "0x38"); // Forward slash key", '-' or '?' (TODO: Test)
        map.put("KEY_CAPS", "0x39"); // Caps Lock key", (TODO: Test)
        map.put("KEY_F1", "0x3A"); // Keyboard F1 key (TODO: Test)
        map.put("KEY_F2", "0x3B"); // Keyboard F2 key (TODO: Test)
        map.put("KEY_F3", "0x3C"); // (TODO: Test)
        map.put("KEY_F4", "0x3D"); // (TODO: Test)
        map.put("KEY_F5", "0x3E"); // (TODO: Test)
        map.put("KEY_F6", "0x3F"); // (TODO: Test)
        map.put("KEY_F7", "0x40"); // (TODO: Test)
        map.put("KEY_F8", "0x41"); // (TODO: Test)
        map.put("KEY_F9", "0x42"); // (TODO: Test)
        map.put("KEY_F10", "0x43"); // (TODO: Test)
        map.put("KEY_F11", "0x44"); // (TODO: Test)
        map.put("KEY_F12", "0x45"); // (TODO: Test)
        map.put("KEY_PRINTSCREEN", "0x46"); // PrintScreen Key (TODO: Test)
        map.put("KEY_SCROLLLOCK", "0x47"); // Scroll Lock key (TODO: Test)
        map.put("KEY_PAUSE", "0x48"); // Pause key (TODO: Test)
        map.put("KEY_INSERT", "0x49"); // Insert key (TODO: Test)
        map.put("KEY_HOME", "0x4A"); // Home key (TODO: Test)
        map.put("KEY_PAGEUP", "0x4B"); // Page up key (TODO: Test)
        map.put("KEY_DELETE", "0x4C"); // Delete Forward key (TODO: Test)
        map.put("KEY_END", "0x4D"); // End key (TODO: Test)
        map.put("KEY_PAGEDOWN", "0x4E"); // Page down key (TODO: Test)
        map.put("KEY_RIGHTARROW", "0x4F"); // Right arrow (TODO: Test)
        map.put("KEY_LEFTARROW", "0x50"); // Left arrow (TODO: Test)
        map.put("KEY_DOWNARROW", "0x51"); // Down arrow (TODO: Test)
        map.put("KEY_UPARROW", "0x52"); // Up arrow (TODO: Test)
        map.put("KEY_NUMLOCK", "0x53"); // Num Lock and clear (TODO: Test)
        map.put("KEY_KPSLASH", "0x54"); // Keypad Forward slash (/) (TODO: Test)
        map.put("KEY_KPASTERISK", "0x55"); // Keypad asterisk (*) (TODO: Test)
        map.put("KEY_KPMINUS", "0x56"); // Keypad minus (-) (TODO: Test)
        map.put("KEY_KPPLUS", "0x57"); // Keypad plus (+) (TODO: Test)
        map.put("KEY_KPENTER", "0x58"); // Keypad Enter", different from keyboard enter (TODO: Test)
        map.put("KEY_KP1", "0x59"); // Keypad 1 and End (TODO: Test)
        map.put("KEY_KP2", "0x5A"); // Keypad 2 and Down arrow (TODO: Test)
        map.put("KEY_KP3", "0x5B"); // Keypad 3 and Page Down (TODO: Test)
        map.put("KEY_KP4", "0x5C"); // Keypad 4 and Left arrow (TODO: Test)
        map.put("KEY_KP5", "0x5D"); // Keypad 5 (TODO: Test)
        map.put("KEY_KP6", "0x5E"); // Keypad 6 and Right arrow (TODO: Test)
        map.put("KEY_KP7", "0x5F"); // Keypad 7 and Home (TODO: Test)
        map.put("KEY_KP8", "0x60"); // Keypad 8 and Up arrow (TODO: Test)
        map.put("KEY_KP9", "0x61"); // Keypad 9 and Page up (TODO: Test)
        map.put("KEY_KP0", "0x62"); // Keypad 0 and Insert (TODO: Test)
        map.put("KEY_KPDOT", "0x63"); // Keypad . and Delete (TODO: Test)
        map.put("KEY_PIPE", "0x64"); // Keyboard '<' or '>' or '|'
        map.put("KEY_MENU", "0x65"); // Keyboard 'Menu/App' (TODO: Test)

        //Modifier Bits
        map.put("KEY_LEFTCTRL", "0x01"); // Keyboard Left Control
        map.put("KEY_LEFTSHIFT", "0x02"); // Keyboard Left Shift
        map.put("KEY_LEFTALT", "0x04"); // Keyboard Left Alt
        map.put("KEY_LEFTGUI", "0x08"); // Keyboard Left GUI", windows key
        map.put("KEY_RIGHTCTRL", "0x10"); // Keyboard Right control
        map.put("KEY_RIGHTSHIFT", "0x20"); // Keyboard Right Shift
        map.put("KEY_RIGHTALT", "0x40"); // Keyboard Right alt
        map.put("KEY_RIGHTGUI", "0x80"); // Keyboard Right GUI", windows key
        return map.get(key);
    }

    /**
     * Convert loop loops through the ducky script and convert to arduino
     * @param code The code that is already created that we append on
     * @param inputArr the array of duckyscript code that needs to be converted.
     * 
     * @return void
     */
    private String convertLoop(String code, String[] inputArr, boolean debug) {
        String last_command = "";
        for(int i = 0; i < inputArr.length; i++){
            last_command = convertCommand(inputArr[i], debug, last_command);
            code = code + last_command;
        }
        return code;
    }

    /**
     * convert single command
     *
     * @param input String of a duckyscript commands
     * 
     * @return String of duckyscript converted to arduino C
     */
    private String convertCommand(String input, boolean debug, String last_command) {
        String ret = "";//return is empty if nothing is set
        String[] parts = input.split(" ");//split everything with space then loop it
        switch (parts[0]) {
            case "REM":
                ret = "  //";
                //start at one since we dont want the 0 since that is REM or other command
                for (int i = 1; i < parts.length; i++) {
                    ret = ret + parts[i] + " ";
                }
                ret = ret + "\n";//add new line
                break;
            case "DELAY":
                //this should always be parts[1] since we are only delaying with a int
                ret = "  delay(" + parts[1] + ");\n";
                break;
            case "REPEAT":
                ret = "  for(int x = 0; x < " + parts[1] + "; x++)\n"
                        + "    " + last_command;
                break;
            case "DEFAULT_DELAY":
                ret = "  delay(" + parts[1] + ");\n";
                break;
            case "STRING":
                String command = "";
                for (int i = 1; i < parts.length; i++) {
                    command += parts[i] + " ";
                }
                command = command.substring(0, command.length()-1);//remove last space SPAAAAACCCCCCCCEEEEEE! (not a portal 2 refference ._.)
                ret = "  execCommand(\"" + command + "\");\n";
                break;
            case "ENTER":
                ret = "  sendKey(" + convertToHex("KEY_ENTER") + ", 0); //KEY_ENTER\n";
                break;
            case "WINDOWS":
            case "GUI":
                ret = "  sendKey(" + convertToHex("KEY_"+parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTGUI") + ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTGUI\n";
                break;
            case "ALT":
                ret = "  sendKey(" + convertToHex("KEY_"+parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTALT") + ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTALT\n";
                break;
            case "SHIFT":
                ret = "  sendKey(" + convertToHex("KEY_"+parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTSHIFT") + ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTSHIFT\n";
                break;
            case "APP":
            case "MENU":
                ret = "  sendKey(" + convertToHex("KEY_MENU") + ", 0);\n";
                break;
            case "CONTROL":
            case "CTRL":
                ret = "  sendKey(" + convertToHex("KEY_"+parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTCTRL") + ");\n";
                break;
            case "DOWNARROW":
            case "DOWN":
                ret = "  sendKey(" + convertToHex("KEY_DOWNARROW") + ", 0);\n";
                break;
            case "UPARROW":
            case "UP":
                ret = "  sendKey(" + convertToHex("KEY_UPARROW") + ", 0);\n";
                break;
            case "LEFTARROW":
            case "LEFT":
                ret = "  sendKey(" + convertToHex("KEY_LEFTARROW") + ", 0);\n";
                break;
            case "RIGHTARROW":
            case "RIGHT":
                ret = "  sendKey(" + convertToHex("KEY_RIGHTARROW") + ", 0);\n";
                break;
            case "BREAK":
            case "PAUSE":
                ret = "  sendKey(" + convertToHex("KEY_PAUSE") + ", 0);\n";
                break;
            case "CAPSLOCK":
                ret = "  sendKey(" + convertToHex("KEY_CAPS") + ", 0);\n";
                break;
            case "DELETE":
                ret = "  sendKey(" + convertToHex("KEY_DELETE") + ", 0);\n";
                break;
            case "END":
                ret = "  sendKey(" + convertToHex("KEY_END") + ", 0);\n";
                break;
            case "ESCAPE":
            case "ESC":
                ret = "  sendKey(" + convertToHex("KEY_ESC") + ", 0);\n";
                break;
            case "HOME":
                ret = "  sendKey(" + convertToHex("KEY_HOME") + ", 0);\n";
                break;
            case "INSERT":
                ret = "  sendKey(" + convertToHex("KEY_INSERT") + ", 0);\n";
                break;
            case "NUMLOCK":
                ret = "  sendKey(" + convertToHex("KEY_NUMLOCK") + ", 0);\n";
                break;
            case "PAGEUP":
                ret = "  sendKey(" + convertToHex("KEY_PAGEUP") + ", 0);\n";
                break;
            case "PAGEDOWN":
                ret = "  sendKey(" + convertToHex("KEY_PAGEDOWN") + ", 0);\n";
                break;
            case "PRINTSCREEN":
                ret = "  sendKey(" + convertToHex("KEY_PRINTSCREEN") + ", 0);\n";
                break;
            case "SCROLLLOCK":
                ret = "  sendKey(" + convertToHex("KEY_SCROLLLOCK") + ", 0);\n";
                break;
            case "SPACE":
                ret = "  sendKey(" + convertToHex("KEY_SPACE") + ", 0);\n";
                break;
            case "TAB":
                ret = "  sendKey(" + convertToHex("KEY_TAB") + ", 0);\n";
                break;
        }
        return ret;
    }
}

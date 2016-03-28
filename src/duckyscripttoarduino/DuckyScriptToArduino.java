/*
* Name:            DuckyScriptToArduino
* Author:          Marcus Orwen
* Copyright:       Copyright (c) 2013, Marcus Orwen
* License:         GNU GPL v3
* License file:    LICENSE.md
* You are NOT authorized to change or remove this comment box.
*/

package duckyscripttoarduino;

import java.io.File;
import java.util.HashMap;

/**
 * Convert almost any ducky script to arduino code so we can use this on Arduino
 * UNO Rev 2 or above (or even Rev 1 cannot test though).
 *
 * TODO: Finish translating keyinputs to Swe/Sv Complete localization files (for other
 * keyboards/languages)
 * 
 * Needs implementation: KEY_MENU (Needs testing) KEY_DOWNARROW (Needs testing)
 * KEY_UPARROW (Needs testing) KEY_LEFTARROW (Needs testing) KEY_RIGHTARROW
 * (Needs testing) KEY_CAPS (Needs testing) KEY_PAUSE (Needs testing) KEY_DELETE
 * (Needs testing) KEY_END (Needs testing) KEY_ESC (Needs testing) KEY_HOME
 * (Needs testing) KEY_INSERT (Needs testing) KEY_NUMLOCK (Needs testing)
 * KEY_PAGEUP (Needs testing) KEY_PAGEDOWN (Needs testing) KEY_PRINTSCREEN
 * (Needs testing) KEY_SCROLLLOCK (Needs testing) KEY_TAB (Needs testing)
 * DEFAULT_DELAY
 * 
 * @author Marcus & ClemaX
 */
public class DuckyScriptToArduino {
	private static FileHandler fileHandler = new FileHandler();
	static File localizationFile = null;

	/**
	 * This will convert most duckyscript codes to arduino C code
	 * 
	 * @param args
	 *            -i input file -o output file -d use debug mode --help or -h
	 *            for displaying commands
	 */
	public static void main(String[] args) {
		// get instance of this class
		DuckyScriptToArduino arduino = new DuckyScriptToArduino();
		String code = null;
		String debugOutput;
		String inputFile = null;
		String localization = null;
		String outputFile = null;
		String debugFile = null;
		boolean debug = false;
		boolean argErr = false;

		String helpStr = "@Author: USB Rubber Ducky and Original Encoder for DuckyScript created by Darren Kitchen.\n"
				+ "@Author: Arduino (Uno) Duckyscript converter created by Marcus Orwén\n\n"
				+ "usage: arduinoduckencode -i [file ..]\t\t\tconvert specified file\n"
				+ "   or: arduinoduckencode -i [file ..] -o [file ..]\tconvert to specified file\n" + "\nArguments:\n"
				+ "   -i [file ..] \t\tInput File\n" + "   -o [file ..] \t\tOutput File\n"
				+ "   -d           \t\tToggle Debug Mode\n" + "   -do [file ..]\t\tToggle Debug File Output Mode\n"
				+ "   -l [file...] \t\tLocalization File\n";

		// no arguments given, show help and exit
		if (args.length == 0) {
			System.out.println(helpStr);
			System.exit(0);
		}

		// Handle args (parameters)
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--help") || args[i].equals("-h")) {
				System.out.println(helpStr);
			} else if (args[i].equals("-o")) {
				outputFile = args[++i];
			} else if (args[i].equals("-i")) {
				inputFile = args[++i];
			} else if (args[i].equals("-d")) {
				debug = true;
			} else if (args[i].equals("-do")) {
				debug = true;// if not set
				debugFile = args[++i];
			} else if (args[i].equals("-l")) {
				localization = args[++i];
			} else {
				System.out.println(helpStr);
				argErr = true;
				break;
			}
		}

		// no arguments given exit
		if (argErr) {
			System.exit(0);
		}
		if (localization == null)
			debugOutput = "Error no localization file!";
		else {
			localizationFile = new File(localization);
		}
		if (inputFile == null) {
			debugOutput = "Error no input file!";
		} else {
			debugOutput = "loading file: " + inputFile + "\n";
		}
		String[] inputArr = fileHandler.loadScript(inputFile);

		debugOutput = debugOutput + "Loaded file!\n";

		debugOutput = debugOutput + "Converting...\n";

		// create base (SWE keyboard info etc)
		code = "uint8_t buf[8] = {0};\n" 
		+ "\n" + "void sendKey(byte key, byte key2){\n"
				+ "  buf[0] = key2;\n"
				+ "  buf[2] = key;\n" 
				+ "  Serial.write(buf, 8);\n" 
				+ "  releaseKey();\n" 
				+ "}\n"
				+ "\n"
				+ "void releaseKey(){\n" 
				+ "  buf[0] = 0;\n" 
				+ "  buf[2] = 0;\n" 
				+"  Serial.write(buf, 8);\n"
				+ "  delay(30); // we can probably go lower but this is as low as i have tested\n" + "}\n" + "\n"
				+ "void execCommand(String command){\n"
				+ "  uint8_t temp_buf[8] = {0};\n"
				+ "  char buffer[1000];\n"
				+ "  command.toCharArray(buffer, command.length() + 1);//convert to char array\n" + "\n"
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
				+ "          temp_buf[0] = 0x02; //KEY_LEFTSHIFT\n" + "          break;\n" + "        case '|':\n"
				+ "        case '@':\n" 
				+ "        case '}':\n" 
				+ "        case '£':\n" 
				+ "        case '$':\n"
				+ "        case '€':\n" 
				+ "        case '{':\n" 
				+ "        case '[':\n"
				+ "        case ']':\n"
				+ "        case '\\\\':\n" // this will result in \\ and then later on \
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
				+ "        temp_buf[2] = 0x2C;//send a space to enter the previous ^\n" + "        break;\n"
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
				+ "        temp_buf[2] = 0x21;\n" + "        break;\n"
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

		code = code + "  Serial.end();\n" + "}";

		// debug output
		if (debug) {
			debugOutput = debugOutput + code + "\n";
		}
		debugOutput = debugOutput + "Convertion finished!\n";
		debugOutput = debugOutput + "Saving...\n";

		// save file, if no output file specified use payload.ino
		debugOutput = debugOutput + fileHandler.save(code, (outputFile == null) ? "payload.ino" : outputFile);

		System.out.println(debugOutput);
		if (debug) {
			fileHandler.save(debugOutput, (debugFile == null) ? "debug.txt" : debugFile);
		}
	}

	/**
	 * ConvertToHex
	 * 
	 * @param key
	 *            need to be in format KEY_* where * is the key, ex KEY_A
	 * @return keyboard hex of the key you want, ex KEY_A = 0x04
	 */

	private String convertToHex(String key) {
		FileHandler.readLocalization(localizationFile);
		HashMap<String, String> map = new HashMap<String, String>();
		for (String Property : fileHandler.getPropertyNames()) {
			System.out.println(Property + ":" + fileHandler.getProperties().getProperty(Property));
			map.put(Property, fileHandler.getProperties().getProperty(Property));
		}
		System.out.println("HashMap generated::" + map);
		return map.get(key);
	}

	/**
	 * Convert loop loops through the ducky script and convert to arduino
	 * 
	 * @param code
	 *            The code that is already created that we append on
	 * @param inputArr
	 *            the array of duckyscript code that needs to be converted.
	 * 
	 * @return void
	 */
	private String convertLoop(String code, String[] inputArr, boolean debug) {
		String last_command = "";
		for (int i = 0; i < inputArr.length; i++) {
			last_command = convertCommand(inputArr[i], debug, last_command);
			code = code + last_command;
		}
		return code;
	}

	/**
	 * convert single command
	 *
	 * @param input
	 *            String of a duckyscript commands
	 * 
	 * @return String of duckyscript converted to arduino C
	 */
	private String convertCommand(String input, boolean debug, String last_command) {
		String ret = "";// return is empty if nothing is set
		String[] parts = input.split(" ");// split everything with space then
											// loop it
		switch (parts[0]) {
		case "REM":
			ret = "  //";
			// start at one since we dont want the 0 since that is REM or other
			// command
			for (int i = 1; i < parts.length; i++) {
				ret = ret + parts[i] + " ";
			}
			ret = ret + "\n";// add new line
			break;
		case "DELAY":
			// this should always be parts[1] since we are only delaying with a
			// int
			ret = "  delay(" + parts[1] + ");\n";
			break;
		case "REPEAT":
			ret = "  for(int x = 0; x < " + parts[1] + "; x++)\n" + "    " + last_command;
			break;
		case "DEFAULT_DELAY":
			ret = "  delay(" + parts[1] + ");\n";
			break;
		case "STRING":
			String command = "";
			for (int i = 1; i < parts.length; i++) {
				command += parts[i] + " ";
			}
			command = command.substring(0, command.length() - 1);
			// remove last space SPAAAAACCCCCCCCEEEEEE! (not a portal 2 reference ._.)
			ret = "  execCommand(\"" + command + "\");\n";
			break;
		case "ENTER":
			ret = "  sendKey(" + convertToHex("KEY_ENTER") + ", 0); //KEY_ENTER\n";
			break;
		case "WINDOWS":
		case "GUI":
			ret = "  sendKey(" + convertToHex("KEY_" + parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTGUI")
					+ ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTGUI\n";
			break;
		case "ALT":
			ret = "  sendKey(" + convertToHex("KEY_" + parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTALT")
					+ ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTALT\n";
			break;
		case "SHIFT":
			ret = "  sendKey(" + convertToHex("KEY_" + parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTSHIFT") 
				+ ");//KEY_" + parts[1].toLowerCase() + " //KEY_LEFTSHIFT\n";
			break;
		case "APP":
		case "MENU":
			ret = "  sendKey(" + convertToHex("KEY_MENU") + ", 0);\n";
			break;
		case "CONTROL":
		case "CTRL":
			ret = "  sendKey(" + convertToHex("KEY_" + parts[1].toUpperCase()) + ", " + convertToHex("KEY_LEFTCTRL")
					+ ");\n";
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

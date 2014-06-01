/*
* Name:            DuckyScriptToArduino
* Author:          Marcus Orwen
* Copyright:       Copyright (c) 2013, Marcus Orwen
* License:         GNU GPL v3
* License file:    LICENSE.md
* You are NOT authorized to change or remove this comment box.
*/

package duckyscripttoarduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus
 */
public class FileHandler {
    /**
     * load
     * @param file input filename with extensions
     * @return String array with line contents
     */
    public String[] load(String file){
        List<String> lines = new ArrayList<>();//create an array list to store the lines we get from BufferReader
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){//read lines untill there is none left
                lines.add(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Failed to load file!\n Error: " + e.getMessage());
        }
        String[] ret = new String[lines.size()];
        lines.toArray(ret);
        return ret;
    }
    /**
     * save
     * @param data The data that should be saved to a file
     * @param dest The destination filename that is going to be saved.
     */
    public String save(String data, String dest){
        String ret;
        //try and write ino file
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dest), true));
            bw.write(data);
            bw.newLine();
            bw.close();
            ret = "Save complete!";
        } catch (Exception e) {
            ret = "Failed to write ino file!\n Error: " + e.getMessage();
        }
        return ret;
    }
}

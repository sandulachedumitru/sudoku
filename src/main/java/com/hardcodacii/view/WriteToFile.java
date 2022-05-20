package com.hardcodacii.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa e folosita pentru scrierea valorilor in fisier.
 * @author Sandulache Dumitru
 */ 

public class WriteToFile {
    private static final String dstFile = "SudokuResults.txt";
    private static BufferedWriter outputFile;
    
    public WriteToFile () {
        try {
            outputFile = new BufferedWriter(new FileWriter(dstFile));
        } catch (IOException ex) {
            System.err.println("Error when processing file in constructor of class WriteToFile(); exiting ... ");
            Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end method
    
    public static void writeToFile (String str) {
        try {
            outputFile.write(str);
            outputFile.flush();
        } catch (IOException ex) {
            System.err.println("Error when processing file in method writeToFile(); exiting ... ");
            Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//end method
    
    public static void closeFile() {
        try {
            outputFile.close();
        } catch (IOException ex) {
            System.err.println("Error when processing file in method closeFile(); exiting ... ");
            Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end method
}//end class

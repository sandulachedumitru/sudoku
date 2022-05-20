package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.view.FileExists;
import com.hardcodacii.view.Tokenize;
import com.hardcodacii.view.WriteToFile;

import java.io.FileNotFoundException;

import java.util.*;
import java.util.logging.*;

import static com.hardcodacii.view.Show.*;

/**
 *
 * @author Sandulache Dumitru
 */
public class Main {
    
    public static void main(String[] args) {
        //se verifica daca user introduce mai mult de un path
        showln(delimiter); showln("USER ARGS CHECK...");
        if (args.length != 1) {
            showlnErr("Must introduce only the name of data file. Format: <file name path>");
            //System.err.println("Must introduce only the name of data file. Format: <file name path>");
            System.exit(-1);
        }
        showln("OK. USER DATA FILE ARGUMENT PASS."); showln(delimiter);

        //se verifica daca fisierul intrudus ezista
        showln("VERIFYING THE EXISTANCE OF THE DATA FILE...");
        FileExists pathOfFileName = new FileExists();
        if (pathOfFileName.exists(args[0])) {
            showln("EXISTANCE OF THE DATA FILE IS CONFIRMED."); showln(delimiter);
            
            //se scaneaza fisierul de caractere care nu sunt litere sau cifre. Acestea sunt inlaturate.
            showln("SCAN THE FILE...");
            Tokenize token = new Tokenize();
            List<String> listOfWords = null;
            try {
                listOfWords = token.tokenize(args[0]);
            } catch (FileNotFoundException fnfe) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, fnfe);
                showlnErr(fnfe);
                showln("SCAN UNSUCCESSFUL. SYSTEM WILL EXIT."); showln(delimiter);
                System.exit(-1);
            }
            showln("SCAN SUCCESSFUL."); showln(delimiter);
            
            //Se analizeaza lista de string-uri serultate
            showln("ANALIZE THE LIST OF TOKENS...");
            List<Integer> listParsed = null;
            if (listOfWords != null) {
                try {
                    listParsed = token.analizeTokenizedList(listOfWords);
                } catch (IllegalArgumentException iae) {
                    showlnErr(iae);
                    showln("ANALIZE UNSUCCESSFUL. SYSTEM WILL EXIT."); showln(delimiter);
                    System.exit(-1);
                }
                //Eliberarea memoriei prin decuplarea variabilelor de structurile de date
                token = null;
                listOfWords = null;
                
                showln("ANALIZE SUCCESSFUL."); showln(delimiter);
            }
            
            //Se initializeaza datele din model
            showln("INITIALIZES BOARD...");
            Board board = new Board(listParsed);
            if ((board.getSetOfCell() == null) || (board.getSetOfSquare() == null)) {
                showlnErr("board.getSetOfCell(): " + board.getSetOfCell() + " and board.getSetOfSquare():" + board.getSetOfSquare());
                showln("INITIALIZATION UNSUCCESSFUL."); showln(delimiter);
                System.exit(-1);
            }
            listParsed = null; //elibereaza memoria
            showBoard(board);
            showln("INITIALIZATION SUCCESSFUL."); showln(delimiter);
            
            //Rezolvarea
            showln("SEARCHING FOR SOLUTIONS...");
            LogicBusiness logicBusiness = new LogicBusiness();
            Solutions solutions = logicBusiness.findSolution(board);
            showSolutions(solutions);
            showln("SOLUTIONS FOUND SUCCESSFUL."); showln(delimiter);
            
        }//end if
        
        WriteToFile.closeFile();
    }//end method  main
}//end class Main

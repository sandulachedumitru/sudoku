package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.service.DisplayService;
import com.hardcodacii.service.FileIOService;
import com.hardcodacii.service.TokenizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.hardcodacii.service.DisplayService.delimiter;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Component
@RequiredArgsConstructor
public class Main {
    private final DisplayService displayService;
    private final FileIOService fileIOService;
    private final TokenizeService tokenizeService;
    private final LogicBusiness logicBusiness;


    public void main(String[] args) {
        //se verifica daca user introduce mai mult de un path
        displayService.showln(delimiter); displayService.showln("USER ARGS CHECK...");
        if (args.length != 1) {
            displayService.showlnErr("Must introduce only the name of data file. Format: <file name path>");
            //System.err.println("Must introduce only the name of data file. Format: <file name path>");
            System.exit(-1);
        }
        displayService.showln("OK. USER DATA FILE ARGUMENT PASS."); displayService.showln(delimiter);

        //se verifica daca fisierul intrudus ezista
        displayService.showln("VERIFYING THE EXISTANCE OF THE DATA FILE...");
        if (fileIOService.exists(args[0])) {
            displayService.showln("EXISTANCE OF THE DATA FILE IS CONFIRMED."); displayService.showln(delimiter);
            
            //se scaneaza fisierul de caractere care nu sunt litere sau cifre. Acestea sunt inlaturate.
            displayService.showln("SCAN THE FILE...");
            List<String> listOfWords = null;
            try {
                listOfWords = tokenizeService.tokenize(args[0]);
            } catch (FileNotFoundException fnfe) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, fnfe);
                displayService.showlnErr(fnfe);
                displayService.showln("SCAN UNSUCCESSFUL. SYSTEM WILL EXIT."); displayService.showln(delimiter);
                System.exit(-1);
            }
            displayService.showln("SCAN SUCCESSFUL."); displayService.showln(delimiter);
            
            //Se analizeaza lista de string-uri serultate
            displayService.showln("ANALIZE THE LIST OF TOKENS...");
            List<Integer> listParsed = null;
            if (listOfWords != null) {
                try {
                    listParsed = tokenizeService.analizeTokenizedList(listOfWords);
                } catch (IllegalArgumentException iae) {
                    displayService.showlnErr(iae);
                    displayService.showln("ANALIZE UNSUCCESSFUL. SYSTEM WILL EXIT."); displayService.showln(delimiter);
                    System.exit(-1);
                }
                //Eliberarea memoriei prin decuplarea variabilelor de structurile de date
                listOfWords = null;

                displayService.showln("ANALIZE SUCCESSFUL."); displayService.showln(delimiter);
            }
            
            //Se initializeaza datele din model
            displayService.showln("INITIALIZES BOARD...");
            Board board = new Board(listParsed);
            if ((board.getSetOfCell() == null) || (board.getSetOfSquare() == null)) {
                displayService.showlnErr("board.getSetOfCell(): " + board.getSetOfCell() + " and board.getSetOfSquare():" + board.getSetOfSquare());
                displayService.showln("INITIALIZATION UNSUCCESSFUL."); displayService.showln(delimiter);
                System.exit(-1);
            }
            listParsed = null; //elibereaza memoria
            displayService.showBoard(board);
            displayService.showln("INITIALIZATION SUCCESSFUL."); displayService.showln(delimiter);
            
            //Rezolvarea
            displayService.showln("SEARCHING FOR SOLUTIONS...");
            Solutions solutions = logicBusiness.findSolution(board);
            displayService.showSolutions(solutions);
            displayService.showln("SOLUTIONS FOUND SUCCESSFUL."); displayService.showln(delimiter);
            
        }//end if
    }//end method  main
}//end class Main

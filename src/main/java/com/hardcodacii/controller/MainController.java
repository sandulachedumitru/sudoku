package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.service.DisplayService;
import com.hardcodacii.service.FileIOService;
import com.hardcodacii.service.TokenizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.util.List;

import static com.hardcodacii.service.DisplayService.delimiter;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Controller
@RequiredArgsConstructor
public class MainController {
    private final static String DEFAULT_FILENAME = "sudoku.txt";

    private final DisplayService displayService;
    private final FileIOService fileIOService;
    private final TokenizeService tokenizeService;
    private final GaneSolver ganeSolver;

    public void start(String[] args) {
        // checks if the user enters filename parameter; more than 1 parameter will be ignored
        displayService.showln(delimiter);
        displayService.showln("USER ARGS CHECK...");
        String fileName;
        if (args.length == 0) {
            fileName = DEFAULT_FILENAME;
            displayService.showln("OK. USER DATA FILE ARGUMENT NOT FOUND. DEFAULT FILE [" + DEFAULT_FILENAME + "] WILL BE SELECTED.");
        } else {
            fileName = args[0];
            displayService.showln("OK. USER DATA FILE ARGUMENT FOUND.");
        }
        displayService.showln(delimiter);

        // checks if the input file exists
        displayService.showln("VERIFYING THE EXISTENCE OF THE INPUT DATA FILE...");
        if (!fileIOService.fileExists(fileName)) {
            displayService.showln("INPUT DATA FILE NOT FOUND. EXITING APP...");
            displayService.showln(delimiter);
            return;
        }
        displayService.showln("EXISTENCE OF THE INPUT DATA FILE IS CONFIRMED.");
        displayService.showln(delimiter);

        // the file is being scanned; characters that are not letters or numbers are ignored
        displayService.showln("SCAN THE FILE...");
        List<String> listOfWords;
        try {
            listOfWords = tokenizeService.tokenize(fileName);
        } catch (FileNotFoundException fnfe) {
            displayService.showlnErr(fnfe);
            displayService.showln("SCAN UNSUCCESSFUL. EXITING APP...");
            displayService.showln(delimiter);
            return;
        }
        displayService.showln("SCAN SUCCESSFUL.");
        displayService.showln(delimiter);

        // analyze the resulting tokens string list
        displayService.showln("ANALIZE THE LIST OF TOKENS...");
        if (listOfWords == null) {
            displayService.showln("CONTENT OF THE INPUT DATA FILE IS NOT VALID. EXITING APP...");
            displayService.showln(delimiter);
            return;
        }
        List<Integer> listParsed;
        try {
            listParsed = tokenizeService.analizeTokenizedList(listOfWords);
        } catch (IllegalArgumentException iae) {
            displayService.showlnErr(iae);
            displayService.showln("ANALIZE UNSUCCESSFUL. SYSTEM WILL EXIT.");
            displayService.showln(delimiter);
            return;
        }
        displayService.showln("ANALIZE SUCCESSFUL.");
        displayService.showln(delimiter);

        // initializes board
        displayService.showln("INITIALIZES BOARD...");
        Board board = new Board(listParsed);
        if ((board.getSetOfCell() == null) || (board.getSetOfSquare() == null)) {
            displayService.showlnErr("board.getSetOfCell(): " + board.getSetOfCell() + " and board.getSetOfSquare():" + board.getSetOfSquare());
            displayService.showln("INITIALIZATION UNSUCCESSFUL.");
            displayService.showln(delimiter);
            return;
        }
        displayService.showBoard(board);
        displayService.showln("INITIALIZATION SUCCESSFUL.");
        displayService.showln(delimiter);

        // finding solutions
        displayService.showln("FINDING SOLUTIONS...");
        Solutions solutions = ganeSolver.findSolution(board);
        displayService.showSolutions(solutions);
        displayService.showln("SOLUTIONS FOUND SUCCESSFUL.");
        displayService.showln(delimiter);
    }
}

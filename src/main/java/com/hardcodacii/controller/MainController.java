package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.service.DisplayService;
import com.hardcodacii.service.FileIOService;
import com.hardcodacii.service.TokenizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static com.hardcodacii.service.DisplayService.delimiterMinus;

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
	private final GameSolver gameSolver;
	private final Solutions solutions;

	public void start(String[] args) {
		// checks if the user enters filename parameter; more than 1 parameter will be ignored
		displayService.showln(delimiterMinus);
		displayService.showln("USER ARGS CHECK...");
		String fileName;
		if (args.length == 0) {
			fileName = DEFAULT_FILENAME;
			displayService.showln("OK. USER DATA FILE ARGUMENT NOT FOUND. DEFAULT FILE [" + DEFAULT_FILENAME + "] WILL BE SELECTED.");
		} else {
			fileName = args[0];
			displayService.showln("OK. USER DATA FILE ARGUMENT FOUND.");
		}
		displayService.showln(delimiterMinus);

		// checks if the input file exists
		displayService.showln("VERIFYING THE EXISTENCE OF THE INPUT DATA FILE...");
		if (!fileIOService.fileExists(fileName)) {
			displayService.showln("INPUT DATA FILE NOT FOUND. EXITING APP...");
			displayService.showln(delimiterMinus);
			return;
		}
		displayService.showln("EXISTENCE OF THE INPUT DATA FILE IS CONFIRMED.");
		displayService.showln(delimiterMinus);

		// the file is being scanned; characters that are not letters or numbers are ignored
		displayService.showln("SCAN THE FILE...");
		List<String> listOfWords;
		try {
			listOfWords = tokenizeService.tokenize(fileName);
		} catch (FileNotFoundException fnfe) {
			displayService.showlnErr(fnfe);
			displayService.showln("SCAN UNSUCCESSFUL. EXITING APP...");
			displayService.showln(delimiterMinus);
			return;
		}
		displayService.showln("SCAN SUCCESSFUL.");
		displayService.showln(delimiterMinus);

		// analyze the resulting tokens string list
		displayService.showln("ANALYZE THE LIST OF TOKENS...");
		if (listOfWords == null) {
			displayService.showln("CONTENT OF THE INPUT DATA FILE IS NOT VALID. EXITING APP...");
			displayService.showln(delimiterMinus);
			return;
		}
		List<Integer> parsedFile;
		try {
			parsedFile = tokenizeService.analizeTokenizedList(listOfWords);
		} catch (IllegalArgumentException iae) {
			displayService.showlnErr(iae);
			displayService.showln("ANALYZE UNSUCCESSFUL. SYSTEM WILL EXIT.");
			displayService.showln(delimiterMinus);
			return;
		}
		displayService.showln("ANALYZE SUCCESSFUL.");
		displayService.showln(delimiterMinus);

		// initializes board
		displayService.showln("INITIALIZES BOARD...");
		Board board = new Board(parsedFile);
		if ((board.getInputCells() == null) || (board.getBoardSquares() == null)) {
			displayService.showlnErr("board.getSetOfCell(): " + board.getInputCells() + " and board.getSetOfSquare():" + Arrays.toString(board.getBoardSquares()));
			displayService.showln("INITIALIZATION UNSUCCESSFUL.");
			displayService.showln(delimiterMinus);
			return;
		}
		displayService.showBoard(board);
		displayService.showln("INITIALIZATION SUCCESSFUL.");
		displayService.showln(delimiterMinus);

		// finding solutions
		displayService.showln("FINDING SOLUTIONS...");
		gameSolver.findSolution(board);
		displayService.showSolutions(solutions);
		displayService.showln("SOLUTIONS FOUND SUCCESSFUL.");
		displayService.showln(delimiterMinus);
	}
}

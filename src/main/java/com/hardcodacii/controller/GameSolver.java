package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * The class specializes in finding the solutions of the game. There may be several solutions for solving the game.
 *
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Component
@RequiredArgsConstructor
public class GameSolver {
	private final Solutions solutions;

	private Board solution;
	private Cell cell;
	private boolean isEndOfProgram, isEndOfSolution, registerSolution, isForward;
	private Integer row, column;

	/**
	 * The method looks for all solutions
	 *
	 * @param solution Board initialized but unresolved
	 * @return the list of all solutions found. If there are no solutions then return null
	 */
	public Solutions findSolution(Board solution) {
		if (solution == null || solution.getBoardCells().length == 0 || solution.getBoardSquares().length == 0 || solution.getSquareDimension() == 0)
			return null;
		this.solution = solution;

		isEndOfProgram = false;
		isEndOfSolution = false;
		registerSolution = false;
		isForward = true;

		row = column = 1;
		cell = solution.getCellFromCoordinate(row, column);
		while (!isEndOfProgram) {
			while (!isEndOfSolution) {
				if (!cell.isFromInput()) {
					if (cell.isEmptyValue()) cell.setValue(0);
					do {
						cell.setValue(cell.getValue() + 1);
					} while (isDuplicatedCellValueInSquareOrRowOrColumn());

					if (cell.getValue() > solution.getBoardDimension()) {
						cell.resetValue();
						backCell();
					} else nextCell();
				} else {
					if (isForward) nextCell();
					else backCell();
				}
			}

			if (registerSolution) {
				solutions.getSolutions().add(Board.copySolutionResults(solution));

				registerSolution = false;
				isEndOfSolution = false;

				backCell();
			}
		}

		return solutions;
	}

	/**
	 * The method checks the existence of a duplicate value on the row / column and its square (2x2, 3x3, 4x4, etc)
	 *
	 * @return true if any, otherwise false
	 */
	private boolean isDuplicatedCellValueInSquareOrRowOrColumn() {
		var squareDimension = solution.getSquareDimension();
		var boardDimension = squareDimension * squareDimension;

		// checked if the value has a duplicate in the "square" to which it belongs
		for (var countCell : cell.getSquareOfCell().getSetOfCell())
			if (!countCell.equals(cell))
				if (cell.getValue().equals(countCell.getValue())) return true;

		// checked if the value of the cell has duplicated in the rows of the matrix, to which it belongs
		for (int row = 1; row <= boardDimension; row++) {
			var countCell = solution.getCellFromCoordinate(row, cell.getColumn());
			if (!countCell.equals(cell))
				if (cell.getValue().equals(countCell.getValue())) return true;
		}

		// checked if the value of the cell has duplicated in the columns of the matrix, to which it belongs
		for (int column = 1; column <= boardDimension; column++) {
			var countCell = solution.getCellFromCoordinate(cell.getRow(), column);
			if (!countCell.equals(cell))
				if (cell.getValue().equals(countCell.getValue())) return true;
		}

		return false;
	}

	/**
	 * Moves to the coordinates of the next cell.
	 * Passing above the last cell  means that a solution has been found.
	 */
	private void nextCell() {
		isForward = true;
		var boardDimension = solution.getBoardDimension();
		if (++column > boardDimension) {
			column = 1;
			if (++row > boardDimension) {
				isEndOfSolution = true;
				registerSolution = true;
			}
		}
		cell = solution.getCellFromCoordinate(row, column);
	}

	/**
	 * Moves to the coordinates of the previous cell.
	 * Passing below the first cell means that the program has finished finding solutions.
	 */
	private void backCell() {
		isForward = false;
		var boardDimension = solution.getBoardDimension();
		if (--column < 1) {
			column = boardDimension;
			if (--row < 1) {
				isEndOfSolution = true;
				isEndOfProgram = true;
			}
		}
		cell = solution.getCellFromCoordinate(row, column);
	}
}

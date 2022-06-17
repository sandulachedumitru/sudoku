package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.service.DisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * The class specializes in finding the solutions of the game. There may be several solutions for solving the game.
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Component
@RequiredArgsConstructor
public class GameSolver {
    private final DisplayService displayService;

    private Board solution = null;
    private Cell cell = null;
    private boolean endOfProgram = false, endOfSolution = false, registerSolution = false, isPresent = false, createSolution = true;
    private Integer row = 1, column = 1;
    
    /**
     * The method looks for all solutions
     * @param solution Board initialized but unresolved
     * @return the list of all solutions found. If there are no solutions then return null
     */
    public Solutions findSolution (Board solution) {
        Solutions solutions = null;
        this.solution = solution;
        while (! endOfProgram) {
            while(! endOfSolution) {
                boolean flag = true;
                cell  = solution.getCellFromCoordinate(row, column);
                if ( ! cell.isFromInput() ) {
                    displayService.showDebug("1CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                    if (cell.getValue() == null) cell.setValue(1);
                    
                    while (flag && checkCellValueInSquareRowColumn()) {
                        displayService.showDebug("2CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                        cell.setValue(cell.getValue() + 1);
                        if ( exceededMaximumCellValueLimit() ) flag = false;
                    }
                    displayService.showDebug("3CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                }//end if (cell.isOriginal())
                else displayService.showDebug("4CELL ORIG:\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                if ( flag ) nextCell();
            }//end while(! endOfSolution)
            while ( endOfSolution && !endOfProgram) {
                if ( registerSolution ) {
                    registerSolution = false;
                    if ( createSolution ) {
                        createSolution = false;
                       // solutions.getInstance().setListOfSolutions(new ArrayList<>());
                        solutions = new Solutions();
                        solutions.setSolutions(new HashSet<>());
                    }
                    solutions.getSolutions().add(Board.copySolutionResults(solution));
                }
                //dupa ce s-a inregistrat solutia se trece la gasirea urmatoarei solutii.
                backCell();
                cell = solution.getCellFromCoordinate(row, column);
                if ( cell != null ) {
                    if ( ! cell.isFromInput() ) {
                        endOfSolution = false;
                        cell.setValue(cell.getValue() + 1);
                        exceededMaximumCellValueLimit();
                    }
                } else {
                    nextCell();
                    cell = solution.getCellFromCoordinate(row, column);
                }
            }//end while ( endOfSolution )
        }//end while (! endOfProgrm)
        return solutions;
    }
    
    /**
     * The method checks the existence of a duplicate value on the row / column and its square (2x2, 3x3, 4x4, etc)
     * @return true if any, otherwise false
     */
    private boolean checkCellValueInSquareRowColumn() {
        var squareDimension = solution.getSquareDimension();
        var boardDimension = squareDimension * squareDimension;

        // checked if the value has a duplicate in the "square" to which it belongs
        for (var countCell : cell.getSquareOfCell().getSetOfCell())
            if ( !countCell.equals(cell) )
                if ( countCell.getValue() == cell.getValue() ) return true;

        // checked if the value of the cell has duplicated in the rows of the matrix, to which it belongs
        for (int row = 1; row <= boardDimension ; row++) {
            var countCell = solution.getCellFromCoordinate(row, cell.getColumn());
            if ( !countCell.equals(cell) )
                if ( countCell.getValue() == cell.getValue() ) return true;
        }

        // checked if the value of the cell has duplicated in the columns of the matrix, to which it belongs
        for (int column = 1; column <= boardDimension ; column++) {
            var countCell = solution.getCellFromCoordinate(cell.getRow(), column);
            if ( !countCell.equals(cell) )
                if ( countCell.getValue() == cell.getValue() ) return true;
        }

        return false;
    }
    
    /**
     * The method checks if the incremented value does not exceed the maximum number allowed.
     * If it exceeds then the value of the cell becomes null, and then it moves to the previous cell.
     * @return true if it exceeded the value and false otherwise.
     */
    private boolean exceededMaximumCellValueLimit() {
        var squareDimension = solution.getSquareDimension();
        var boardDimension = squareDimension * squareDimension;

        while (cell.getValue() > boardDimension) {
            cell.setValue(null);
            displayService.showDebug("5CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());

            do {
                cell = backCell();
                //if cell == null then it means that all the solutions have been found. Return to main ().
                if ( cell == null ) {
                    cell = nextCell();
                    return true;
                }
            } while (cell.isFromInput());

            cell.setValue(cell.getValue() + 1);
            if ( cell.getValue() <= boardDimension ) return true; //TODO why return true, instead of false
        }

        return false;
    }
    
    /**
     * Moves to the coordinates of the next cell.
     * Passing above the last cell  means that a solution has been found.
     */
    private Cell nextCell () {
        var squareDimension = solution.getSquareDimension();
        var boardDimension = squareDimension * squareDimension;
        if ( ++column > boardDimension ) {
            column = 1;
            if ( ++row > boardDimension ) {
                endOfSolution = true;
                registerSolution = true;
            }
        }
        return solution.getCellFromCoordinate(row, column);
    }
    
    /**
     * Moves to the coordinates of the previous cell.
     * Passing below the first cell means that the program has finished finding solutions.
     */
    private Cell backCell () {
        var squareDimension = solution.getSquareDimension();
        var boardDimension = squareDimension * squareDimension;
        if ( --column < 1 ) {
            column = boardDimension;
            if ( --row < 1 ) {
                endOfSolution = true;
                endOfProgram = true;
            }
        }
        return solution.getCellFromCoordinate(row, column);
    }

}

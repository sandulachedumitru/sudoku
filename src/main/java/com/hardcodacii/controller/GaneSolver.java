package com.hardcodacii.controller;

import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.service.DisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * The class specializes in finding the solutions of the game. There may be several solutions for solving the game.
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Component
@RequiredArgsConstructor
public class GaneSolver {
    private final DisplayService displayService;

    private Board solution = null;
    private Cell cell = null;
    private boolean endOfProgram = false, endOfSolution = false, registerSolution = false, isPresent = false, createSolution = true;
    private Integer row = 1, column = 1;
    
    /**
     * Metoda cauta toate rezolvarile 
     * @param solution Boar initializat dar nerezolvat
     * @return lista tuturor solutiilor gasite. Daca nu exista solutii atunci returneaza null
     */
    public Solutions findSolution (Board solution) {
        Solutions solutions = null;
        this.solution = solution;
        while (! endOfProgram) {
            while(! endOfSolution) {
                boolean flag = true;
                cell  = solution.getCellFromCoordinate(row, column);
                if ( ! cell.isOriginal() ) {
                    displayService.showDebug("1CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                    if (cell.getValue() == null) cell.setValue(1);
                    
                    while (flag && checkCellValueInSquareRowColumn()) {
                        displayService.showDebug("2CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
                        cell.setValue(cell.getValue() + 1);
                        if ( checkMaxLimitOfCellValue() ) flag = false;
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
                        solutions.setListOfSolutions(new ArrayList<>());
                    }
                    solutions.getListOfSolutions().add(Board.copySolutionResults(solution));
                }
                //dupa ce s-a inregistrat solutia se trece la gasirea urmatoarei solutii.
                backCell();
                cell = solution.getCellFromCoordinate(row, column);
                if ( cell != null ) {
                    if ( ! cell.isOriginal() ) {
                        endOfSolution = false;
                        cell.setValue(cell.getValue() + 1);
                        checkMaxLimitOfCellValue();
                    }
                } else {
                    nextCell();
                    cell = solution.getCellFromCoordinate(row, column);
                }
            }//end while ( endOfSolution )
        }//end while (! endOfProgrm)
        return solutions;
    }//end method findSolution
    
    /**
     * Metoda verifica existenta unei valori pe linia/coloana si patratelul (2x2, 3x3, 4x4, etc) ei
     * @return true daca exista, altfel false
     */
    private boolean checkCellValueInSquareRowColumn() {
        boolean isExisting = false;
        Cell countCell;
        //se verifica daca valoarea celulei se regaseste in liniile/coloanele matricei apoi
        //se verifica daca se regaseste in "square" de care apartine
        if ( !isExisting )
            for (Cell count : cell.getSquareOfCell().getSetOfCell())
                if ( !count.equals(cell) )
                    if ( count.getValue() == cell.getValue() ) isExisting = true;        
        if ( !isExisting )
            for (int row = 1;  row <= (solution.getNumberOfRowsAndColumns() * solution.getNumberOfRowsAndColumns()) ; row++) {
                countCell = solution.getCellFromCoordinate(row, cell.getColumn());
                if ( !countCell.equals(cell) )
                    if ( countCell.getValue() == cell.getValue() ) isExisting = true;
            }
        if ( !isExisting )
            for (int column = 1;  column <= (solution.getNumberOfRowsAndColumns() * solution.getNumberOfRowsAndColumns()) ; column++) {
                countCell = solution.getCellFromCoordinate(cell.getRow(), column);
                if ( !countCell.equals(cell) )
                    if ( countCell.getValue() == cell.getValue() ) isExisting = true;
            }
        return isExisting;
    }//end method checkCellValueInSquareRowColumn
    
    /**
     * Metoda verifica daca valoarea incrementata nu depaseste numarul maxim admis.
     * Daca depaseste atunci valoarea celulei devine null apoi se trece la celula anterioara.
     * @return true daca a depasit valoarea si false daca nu a depasit valoarea.
     */
    private boolean checkMaxLimitOfCellValue () {
        Integer numberOfRowsAndColumns = solution.getNumberOfRowsAndColumns();
        while (cell.getValue() > (numberOfRowsAndColumns*numberOfRowsAndColumns) ) {
            cell.setValue(null);
            displayService.showDebug("5CELL:\t\trow:" + cell.getRow() + "\tcolumn:" + cell.getColumn() + "\tvalue:" + cell.getValue());
            boolean flagOriginal = true;
            //se verifica ca atunci cand backCell celula sa nu fie originara.
            while ( flagOriginal ) {
                backCell();
                cell = solution.getCellFromCoordinate(row, column);
                //daca cell == null atunci inseamna ca s-a terminat de gasit toate solutiile. Se revine in main().
                if ( cell == null ) {
                    nextCell();
                    cell = solution.getCellFromCoordinate(row, column);
                    return true;
                }
                if ( ! cell.isOriginal() ) flagOriginal = false;
            }
            cell.setValue(cell.getValue() + 1);
            if ( cell.getValue() <= (numberOfRowsAndColumns*numberOfRowsAndColumns) ) return true;
        }//end while
        return false;
    }
    
    /**
     * Face trecerea la coordonatele celulei urmatoare.
     * Atunci cand se trece mai sus de ultima celula atunci inseamna ca s-a gasit o solutie.
     */
    private void nextCell () {
        Integer numberOfRowsAndColumns = solution.getNumberOfRowsAndColumns();
        if ( ++column > numberOfRowsAndColumns*numberOfRowsAndColumns ) {
            column = 1;
            if ( ++row > numberOfRowsAndColumns*numberOfRowsAndColumns ) {
                endOfSolution = true;
                registerSolution = true;
            }
        }
    }//end method
    
    /**
     * Face trecerea la coordonatele celulei anterioare.
     * Atunci cand se trece mai jos de prima celula inseamna ca programul a terminat de gasit solutii.
     */
    private void backCell () {
        Integer numberOfRowsAndColumns = solution.getNumberOfRowsAndColumns();        
        if ( --column < 1 ) {
            column = numberOfRowsAndColumns*numberOfRowsAndColumns;
            if ( --row < 1 ) {
                endOfSolution = true;
                endOfProgram = true;
            }
        }
    }//end method

}

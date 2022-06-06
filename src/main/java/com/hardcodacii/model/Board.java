package com.hardcodacii.model;

import lombok.Getter;

import java.util.*;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Getter
public class  Board {

    private enum CoordinateType {
        // the order in which the enumerations are written is important
        ROW, COLUMN, VALUE
    }
    
    private int numberOfRowsAndColumns = 0;
    private Set<Cell> setOfCell = null;
    private Set<Square> setOfSquare = null;
    private List<List<Cell>> arrayListOfCell = null;

    // used internally
    private Board() {}

    /**
     * Constructor in which the game grid is initialized.
     * @param listFileParsed list of values taken from the input file. The first value in the list is the number of rows and columns of squares (format: 2x2, 3x3, 4x4, etc).
     * For a square N x N then the number of cells = N ^ 2, rows = columns = N (ex: 3x3 => 3 ^ 2 = 9)
     */
    public Board(List<Integer> listFileParsed) {
        // the first positive integer from the input file which specifies the number of rows and columns from a square
        boolean isFirst = true;
        // specify that a cell that has value could be built. A valid cell has coordinates and value (row, column, value)
        boolean makeCell = true;
        // specify that a cell has all three values (row, column, value) and can be built
        boolean pass = false;
        // count is a number from 1 to 3 that specifies for a cell whether a number is
        // either for a row, for a column, or for a cell value (0 for row, 1 for column, 2 for value)
        int count = 1;

        Cell cell = null; Square square;

        // create the set of all cells defined in the input file
        for (Integer listInt : listFileParsed) {
            if (makeCell) {
                cell = new Cell();
                makeCell = false;
            }

            if (isFirst) {
                numberOfRowsAndColumns = listInt;
                isFirst = false;               
            } else {
                if (count <= CoordinateType.values().length) {
                    switch (CoordinateType.values()[count-1]) {
                        case ROW:
                            cell.setRow(listInt);
                            break;
                        case COLUMN:
                            cell.setColumn(listInt);
                            break;
                        case VALUE:
                            cell.setValue(listInt);
                            pass = true;
                            break;
                    }
                    count++;
                } //end if
                if (pass) {
                    cell.setOriginal(true);
                    
                    count = 1;
                    makeCell = true; pass = false;
                    
                    if (setOfCell == null) setOfCell = new HashSet<>();
                    setOfCell.add(cell);
                }//end if
            }//end else
        }//end for
        
        // create and init the actual board (arrayListOfCell)
        if (setOfCell != null) {
            //copy setOfCell into soc
            Iterator<Cell> iteratorCell = setOfCell.iterator();
            Set<Cell> soc = new HashSet<>();
            while (iteratorCell.hasNext()) soc.add(iteratorCell.next());

            // init the board
            int boardDimension = numberOfRowsAndColumns*numberOfRowsAndColumns;
            arrayListOfCell = new ArrayList<>();
            List<Cell> listOfCell;
            for (int row = 1; row <= (boardDimension); row++) {
                listOfCell = new ArrayList<>();
                arrayListOfCell.add(listOfCell);
                for (int column = 1; column <= (boardDimension); column++) {
                    cell = new Cell(row , column);

                    boolean flagCell = true;
                    Cell localCell;
                    Iterator<Cell> iterator = soc.iterator();
                    while (flagCell && iterator.hasNext()) {
                        localCell = iterator.next();
                        if (cell.equals(localCell)) {
                            flagCell = false;
                            cell = localCell;
                            soc.remove(localCell);
                        }
                    }
                    Integer rowOfSquare = ((cell.getRow() % numberOfRowsAndColumns) == 0) ? (cell.getRow() / numberOfRowsAndColumns) : (cell.getRow() / numberOfRowsAndColumns) + 1;
                    Integer columnOfSquare = ((cell.getColumn() % numberOfRowsAndColumns) == 0) ? (cell.getColumn() / numberOfRowsAndColumns) : (cell.getColumn() / numberOfRowsAndColumns) + 1;
                    square = new Square(rowOfSquare, columnOfSquare);
                    
                    if (setOfSquare != null) {
                        if (setOfSquare.contains(square)) {
                            Iterator<Square> it = setOfSquare.iterator();
                            boolean flagSquare = true;
                            Square sq;
                            while (flagSquare && it.hasNext()) {
                                sq = it.next();
                                if (sq.equals(square)) {
                                    flagSquare = false;
                                    square = sq;
                                }
                            }
                            if (square.getSetOfCell() != null) {
                                square.getSetOfCell().add(cell);
                            } else {
                                square.setSetOfCell(new HashSet<>());
                                square.getSetOfCell().add(cell);
                            }
                        } else {
                            square.setSetOfCell(new HashSet<>());
                            square.getSetOfCell().add(cell);
                            setOfSquare.add(square);
                        }
                    } else {
                        setOfSquare = new HashSet<>();
                        square.setSetOfCell(new HashSet<>());
                        square.getSetOfCell().add(cell);
                        setOfSquare.add(square);
                    }
                    
                    cell.setSquareOfCell(square);
                    listOfCell.add(cell);
                }//end for column
            }//end for row
        }//end if
        
    }//end constructor
    
    /**
     * The method finds a cell according to its row and column.
     * @param row cell's row
     * @param column cell's column
     * @return celula aflata. Daca linia si coloana nu se incadreaza in spectrul de valori atunci se returneaza o celula == null.
     */
    public Cell getCellFromCoordinate(Integer row, Integer column) {
        if ((1 <= row) && (row <= numberOfRowsAndColumns*numberOfRowsAndColumns) && (1 <= column) && (column <= numberOfRowsAndColumns*numberOfRowsAndColumns)) {
            Cell cell;
            List<Cell> listOfCell;
            Iterator<List<Cell>> listIterator = arrayListOfCell.iterator();
            while (listIterator.hasNext()) {
                listOfCell = listIterator.next();
                Iterator<Cell> cellIterator = listOfCell.iterator();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    if ((cell.getRow() == row) && (cell.getColumn() == column)) return cell;
                }
            }
        }//end if
        return null;
    }
    
    /**
     * Metoda copiaza arrayListOfCell. Metoda e folosita ptr a inregistra solutiile din metoda findSolaution(Board solution).
     * @param board solutia
     * @return copie arrayListOfCell a solutiei
     */
    public static Board copySolutionResults (Board board) {
        Board boardCopy = null;
        List<Cell> copyListOfCells = null;
        List<List<Cell>> copyArrayListOfCell = null;
        
        if ( board != null ) {
            boardCopy = new Board();
            
            boardCopy.numberOfRowsAndColumns = board.numberOfRowsAndColumns;

            Iterator<List<Cell>> listIterator = board.arrayListOfCell.iterator();
            List<Cell> listOfCells;
            if ( listIterator.hasNext() ) copyArrayListOfCell = new ArrayList<>();
            while (listIterator.hasNext()) {
                listOfCells = listIterator.next();
                Iterator<Cell> cellIterator = listOfCells.iterator();
                Cell cell;
                copyListOfCells = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    Cell cellClone = new Cell();
                    cellClone.setRow(cell.getRow());
                    cellClone.setColumn(cell.getColumn());
                    cellClone.setValue(cell.getValue());
                    cellClone.setOriginal(cell.isOriginal());
                    copyListOfCells.add(cellClone);
                }
                copyArrayListOfCell.add(copyListOfCells);
            }//end while
            boardCopy.arrayListOfCell = copyArrayListOfCell;
        }//end if
        
        return boardCopy;
    }
    
}//end class

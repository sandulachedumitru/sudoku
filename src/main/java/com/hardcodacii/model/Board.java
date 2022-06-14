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

    private int squareDimension;
    private int boardDimension;
    private Set<Cell> inputCells = new HashSet<>();;
    private Square[] boardSquares;
    private Cell[] boardCells;

    // used internally
    private Board() {}

    /**
     * Constructor in which the game grid is initialized.
     * @param parsedFile list of values taken from the input file. The first value in the list is the number of rows and columns of squares (format: 2x2, 3x3, 4x4, etc).
     * For a square N x N then the number of cells = N ^ 2, rows = columns = N (ex: 3x3 => 3 ^ 2 = 9)
     */
    public Board(List<Integer> parsedFile) {
        if (parsedFile != null) {
            createCellsFromInputFile(parsedFile);
            boardInit();
        }
    }

    private void createCellsFromInputFile(List<Integer> parsedFile) {
        // the first positive integer from the input file which specifies the number of rows and columns from a square
        boolean isFirst = true;
        // count is a number from 1 to 3 that specifies for a cell whether a number is
        // either for a row, for a column, or for a cell value (0 for row, 1 for column, 2 for value)
        int count = 1;

        // create the set of all cells defined in the input file
        int row = 0, col = 0, value = 0;
        for (Integer listInt : parsedFile) {
            if (isFirst) {
                squareDimension = listInt;
                isFirst = false;
            } else {
                if (count <= CoordinateType.values().length) {
                    switch (CoordinateType.values()[count-1]) {
                        case ROW:
                            row = listInt;
                            break;
                        case COLUMN:
                            col = listInt;
                            break;
                        case VALUE:
                            value = listInt;

                            Cell cell = new Cell(row, col);
                            cell.setValue(value);
                            cell.setFromInput(true);
                            inputCells.add(cell);

                            count = 1;
                            break;
                    }
                    count++;
                }
            }
        }
    }

    private void boardInit() {

        // define the board and its elements
        boardDimension = squareDimension * squareDimension;
        boardCells = new Cell[boardDimension * boardDimension];
        boardSquares = new Square[boardDimension];

        // init the element with coordinates
        initElementWithCoordinates(boardCells);
        initElementWithCoordinates(boardSquares);

        // init board with input value
        for (Cell inpCell : inputCells) {
            Cell cell = getCellFromCoordinate(inpCell.getRow(), inpCell.getColumn());
            cell.setValue(inpCell.getValue());
            cell.setFromInput(true);
        }

        // link cell with square
        for (Cell cell : boardCells) {
            Square square = getSquareFromCell(cell);
            square.getSetOfCell().add(cell);
            cell.setSquareOfCell(square);
        }
    }

    private void initElementWithCoordinates(Element[] element) {
        Double sqrt = Math.sqrt(element.length);
        int dimension = sqrt.intValue();
        for (int row = 1; row <= element.length; row++)
            for (int col = 1; col <= element.length; col++) {
                Element el = element[(row  -1) * dimension + (col -1)];
                el.setRow(row);;
                el.setColumn(col);
            }
    }

    public Cell getCellFromCoordinate(int row, int column) {
        int boardDimension = squareDimension * squareDimension;
        if ((1 <= row) && (row <= boardDimension) && (1 <= column) && (column <= boardDimension) && boardCells.length == boardDimension * boardDimension)
            return boardCells[(row -1) * boardDimension + (column -1)];
        return null;
    }

    public Square getSquareFromCell(Cell cell) {
        int squareRow = (cell.getRow() % squareDimension) == 0 ? (cell.getRow() / squareDimension) : (cell.getRow() / squareDimension) + 1;
        int  squareColumn = (cell.getColumn() % squareDimension == 0) ? (cell.getColumn() / squareDimension) : (cell.getColumn() / squareDimension) + 1;
        return new Square(squareRow, squareColumn);
    }

    public Square getSquareFromCoordinate(int row, int column) {
        int boardDimension = squareDimension * squareDimension;
        if ((1 <= row) && (row <= squareDimension) && (1 <= column) && (column <= squareDimension) && boardSquares.length == boardDimension)
            return boardSquares[(row -1) * squareDimension + (column -1)];
        return null;
    }
    
    /**
     * @param board solution
     * @return arrayListOfCell solution copy
     */
    public static Board copySolutionResults (Board board) {
        Board boardCopy = null;
        List<Cell> copyListOfCells = null;
        List<List<Cell>> copyArrayListOfCell = null;
        
        if ( board != null ) {
            boardCopy = new Board();
            
            boardCopy.squareDimension = board.squareDimension;

            Iterator<List<Cell>> listIterator = board.boardCells.iterator();
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
                    cellClone.setFromInput(cell.isFromInput());
                    copyListOfCells.add(cellClone);
                }
                copyArrayListOfCell.add(copyListOfCells);
            }//end while
            boardCopy.boardCells = copyArrayListOfCell;
        }//end if
        
        return boardCopy;
    }
    
}//end class

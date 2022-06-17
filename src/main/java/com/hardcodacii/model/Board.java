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
    private final Set<Cell> inputCells = new HashSet<>();
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
        boolean isCellCreated = false;

        int row = 0, col = 0;

        // create the set of all cells defined in the input file
        for (Integer listInt : parsedFile) {
            if (isFirst) {
                squareDimension = listInt;
                boardDimension = squareDimension * squareDimension;
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
                            Cell cell = new Cell(row, col);
                            cell.setValue(listInt);
                            cell.setFromInput(true);
                            inputCells.add(cell);
                            isCellCreated = true;

                            count = 1;
                            break;
                    }
                    if (isCellCreated) {
                        isCellCreated = false;
                        count = 1;
                    } else count++;
                }
            }
        }
    }

    private void boardInit() {

        // define the board and its elements
        boardCells = new Cell[boardDimension * boardDimension];
        boardSquares = new Square[boardDimension];

        // init the element with coordinates
        initCellWithCoordinates(boardCells);
        initSquareWithCoordinates(boardSquares);

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

    private void initCellWithCoordinates(Cell[] cells) {
        int dimension = (int) Math.sqrt(cells.length);
        for (int row = 1; row <= dimension; row++)
            for (int col = 1; col <= dimension; col++)
                cells[(row  -1) * dimension + (col -1)] = new Cell(row, col);
    }

    private void initSquareWithCoordinates(Square[] squares) {
        int dimension = (int) Math.sqrt(squares.length);
        for (int row = 1; row <= dimension; row++)
            for (int col = 1; col <= dimension; col++)
                squares[(row  -1) * dimension + (col -1)] = new Square(row, col);
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
    public static Board copySolutionResults(Board board) {
        return board != null ? (Board) board.clone() : null;
    }

    @Override
    public Object clone() {
        Cell[] boardCellClone = new Cell[this.boardCells.length];
        int index = 0;
        for (Cell cell : this.boardCells) {
            var cellClone = new Cell();
            cellClone.setRow(cell.getRow());
            cellClone.setColumn(cell.getColumn());
            cellClone.setValue(cell.getValue());
            cellClone.setSquareOfCell(cell.getSquareOfCell());
            boardCellClone[index++] = cellClone;
        }

        Board boardCopy;
        try {
            boardCopy = (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            boardCopy = new Board();
        }
        boardCopy.squareDimension = this.squareDimension;
        boardCopy.boardDimension = this.boardDimension;
        boardCopy.boardSquares = this.boardSquares.clone();
        boardCopy.boardCells = boardCellClone;

        return boardCopy;
    }
}

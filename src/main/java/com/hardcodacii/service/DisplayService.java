package com.hardcodacii.service;


import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.model.Square;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Show logs and results
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class DisplayService {
    private boolean SHOW = true;
    private boolean SHOW_DEBUG = false;
    public static final String delimiterMinus = "------------------------------------------------------------------";
    private static final String delimiterEqual = "=======================================================================================";

    private final LogChacheService logChacheService;
    
    public void showln(Object obj) {
        if (SHOW) {
            System.out.println(obj);
            logChacheService.showln(obj);
        }
    }

    public void show(Object obj) {
        if (SHOW) {
            System.out.print(obj);
            logChacheService.show(obj);
        }
    }

    public void showlnErr(Object obj) {
        if (SHOW) {
            String suffix = logChacheService.getErrorSuffix();
            System.err.println(suffix + obj);
            logChacheService.showlnErr(obj);
        }
    }

    public void showErr(Object obj) {
        if (SHOW) {
            String suffix = logChacheService.getErrorSuffix();
            System.err.print(suffix + obj);
            logChacheService.showErr(obj);
        }
    }
    
    public void showBoard (Board board) {
        int countSquare = 1;
        int countCell;

        showln("\tNUMBER OF ROWS AND COLUMNS: " + board.getSquareDimension()); showln("");
        showln("\tLISTING OF SQUARE");
        for (Square square : board.getBoardSquares()) {
            show("\tSquare[" + countSquare + "]:");
            show("\tROW: " + square.getRow());
            show("\tCOLUMN: " + square.getColumn()); showln("");
            countCell = 1;
            for (Cell cell : square.getSetOfCell()) {
                show("\t\t");
                show("CELL[" + countCell++ + "]:");
                show(" row:" + cell.getRow());
                show(" column:" + cell.getColumn());
                show(" value:" + cell.getValue());
                show(" square["); 
                show("ROW:" + cell.getSquareOfCell().getRow()); show(",");
                show("COLUMN:" + cell.getSquareOfCell().getColumn());
                show("]");
                showln("");
            }
            countSquare++;
        }
        showln("");
        
        countCell = 1;
        showln("\tLISTING OF INITIAL CELLS");
        for(Cell inputCell : board.getInputCells()) {
            Cell cell = board.getCellFromCoordinate(inputCell.getRow(), inputCell.getColumn());
            show("\tCell[" + countCell++ + "]:");
            show("\trow: " + cell.getRow());
            show("\tcolumn: " + cell.getColumn());
            show("\tvalue: " + cell.getValue());
            show("\tSquare: ");
            show("ROW:" + cell.getSquareOfCell().getRow());
            showln("\tCOLUMN:" + cell.getSquareOfCell().getColumn());
        }//end for
        showln("");
        
        // display the board
        showln("\tDISPLAYS BOARD");
        displayBoardWithSquare(board);
        showln("");
    }
    
    public void showSolutions (Solutions solutions) {
        if ( solutions != null ) {
            var countSolutions = 0;
            for (var board : solutions.getSolutions()) {
                showln("\tSOLUTION:" + ++countSolutions);
                displayBoardWithSquare(board);
                showln("");
            }
        }
    }

    private void displayBoardWithSquare(Board board) {
        System.out.print("\t");
        int dimension = 1;
        int squareParagraph = 1;
        for (var cell : board.getBoardCells()) {
            String value = cell.getValue() != null ? cell.getValue().toString() : " ";

            System.out.print("\t[" + cell.getRow() + "-" + cell.getColumn() + "-" + value + "]");
            if (dimension % board.getSquareDimension() == 0) {
                if (dimension == board.getBoardDimension()) {
                    squareParagraph++;
                    dimension = 0;

                    if (squareParagraph++ % board.getSquareDimension() == 0) {
                        squareParagraph = 1;
                        System.out.println();
                    }
                    System.out.println();
                    System.out.print("\t");
                } else
                    System.out.print("\t");
            }

            dimension++;
        }
    }

    private void displayBoardCompact(Board board) {
        show("\t");
        int dimension = 1;
        for (var cell : board.getBoardCells()) {
            String value = cell.getValue() != null ? cell.getValue().toString() : " ";

            show("\t[" + cell.getRow() + "-" + cell.getColumn() + "-" + value + "]");
            if (dimension == board.getBoardDimension()) {
                dimension = 1;
                showln("");
                show("\t");
            } else
                dimension++;
        }
    }
    
    public void showDebug(Object obj) {
        if (SHOW_DEBUG) showln(obj);
    }

    public void setShow(boolean show) {
        SHOW = show;
    }

    public void setShowDebug(boolean showDebug) {
        SHOW_DEBUG = showDebug;
    }
}

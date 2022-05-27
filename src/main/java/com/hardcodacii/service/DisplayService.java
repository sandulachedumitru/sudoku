package com.hardcodacii.service;


import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.model.Square;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Show logs and results
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class DisplayService {
    private boolean SHOW = true;
    private boolean SHOW_DEBUG = false;
    public static final String delimiter = "------------------------------------------------------------------";

    private final LogChacheService logChacheService;
    
    public void showln(Object obj) {
        if (SHOW) {
            System.out.println(obj);
            logChacheService.showln(obj);
        }
    }//end method

    public void show(Object obj) {
        if (SHOW) {
            System.out.print(obj);
            logChacheService.show(obj);
        }
    }//end method

    public void showlnErr(Object obj) {
        if (SHOW) {
            String suffix = logChacheService.getErrorSuffix();
            System.err.println(suffix + obj);
            logChacheService.showlnErr(obj);
        }
    }//end method

    public void showErr(Object obj) {
        if (SHOW) {
            String suffix = logChacheService.getErrorSuffix();
            System.err.print(suffix + obj);
            logChacheService.showErr(obj);
        }
    }//end method
    
    public void showBoard (Board board) {
        int countSquare = 1;
        int countCell = 1;
        showln("\tNUMBER OF ROWS AND COLUMNS: " + board.getNumberOfRowsAndColumns()); showln("");
        showln("\tLISTING OF SQUARE");
        for (Square square : board.getSetOfSquare()) {
            show("\tSquare[" + countSquare + "]:");
            show("\tROW: " + square.getRow());
            show("\tCOLUMN: " + square.getColumn()); showln("");
            countCell = 1;
            for (Cell cell : square.getSetOfCell()) {
                show("\t\t");
                show("CELL[" + countCell + "]:");
                show(" row:" + cell.getRow());
                show(" column:" + cell.getColumn());
                show(" value:" + cell.getValue());
                show(" square["); 
                show("ROW:" + cell.getSquareOfCell().getRow()); show(",");
                show("COLUMN:" + cell.getSquareOfCell().getColumn());
                show("]");
                showln("");
                countCell++;
            }
            countSquare++;
        }//end for
        showln("");
        
        countCell = 1;
        countSquare = 1;
        showln("\tLISTING OF INITIAL CELLS");
        for(Cell cell : board.getSetOfCell()) {
            show("\tCell[" + countCell + "]:");
            show("\trow: " + cell.getRow());
            show("\tcolumn: " + cell.getColumn());
            show("\tvalue: " + cell.getValue());
            show("\tSquare: ");
            show("ROW:" + cell.getSquareOfCell().getRow());
            showln("\tCOLUMN:" + cell.getSquareOfCell().getColumn());
            countCell++;
        }//end for
        showln("");
        
        //afiseaza board.arrayListOfCell
        Cell cell;
        List<Cell> listOfCell;
        int row = 1, column = 1;
        Iterator<List<Cell>> listIterator = board.getArrayListOfCell().iterator();
        showln("\tDISPLAYS BOARD ARRAY");
        while (listIterator.hasNext()) {
            listOfCell = listIterator.next();
            Iterator<Cell> cellIterator = listOfCell.iterator();
            
            show("\tRow[" + row + "]: ");
            row++;
            column = 1;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                
//                show("\tCol[" + column + "]:" + cell.getValue() + "\\" + cell.getSquareOfCell() + "[" + cell.getSquareOfCell().getRow() + 
//                        "," + cell.getSquareOfCell().getColumn() + "]");
                show("\tCol[" + column + "]:" + cell.getValue());

                column++;
            }
            showln("");
        }
        showln("");
    }//end method
    
    public void showSolutions (Solutions solutions) {
        if ( solutions != null ) {
            Iterator<Board> listOfSolutions = solutions.getListOfSolutions().iterator();
            int countSolutions = 0;
            while (listOfSolutions.hasNext()) {
                showln("\tSOLUTION:" + ++countSolutions);
                Iterator<List<Cell>> listIterator = listOfSolutions.next().getArrayListOfCell().iterator();
                List<Cell> listOfCell;
                while (listIterator.hasNext()) {
                    listOfCell = listIterator.next();
                    Iterator<Cell> cellIterator = listOfCell.iterator();
                    Cell cell;
                    show("\t");
                    while (cellIterator.hasNext()) {
                        cell = cellIterator.next();
                        show("\t[" + cell.getRow() + "-" + cell.getColumn() + "-" + cell.getValue() + "]");
                    }
                    showln("");
                }
            }
        }
    }//end method
    
    public void showDebug(Object obj) {
        if (SHOW_DEBUG) showln(obj);
    }

    public void setShow(boolean show) {
        SHOW = show;
    }

    public void setShowDebug(boolean showDebug) {
        SHOW_DEBUG = showDebug;
    }
}//end class

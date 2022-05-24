package com.hardcodacii.service;


import com.hardcodacii.model.Board;
import com.hardcodacii.model.Cell;
import com.hardcodacii.model.Solutions;
import com.hardcodacii.model.Square;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Show result
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class DisplayServiceImpl implements DisplayService {
    private boolean SHOW = true;
    private boolean SHOW_DEBUG = false;
    public static final String delimiter = "------------------------------------------------------------------";

    private final FileIOService fileIOService;
    
    public void showln(Object obj) {
        if (SHOW) {
            System.out.println(obj);
            fileIOService.writeToFile(obj.toString() + "\r\n");
        }
    }//end method
    
    public void show(Object obj) {
        if (SHOW) {
            System.out.print(obj);
            fileIOService.writeToFile(obj.toString());
        }
    }//end method
    
    public void showlnErr(Object obj) {
        if (SHOW) {
            System.err.println(" -ERROR:" + obj);
            fileIOService.writeToFile(" -ERROR:" + obj.toString() + "\r\n");
        }
    }//end method
    
    public void showErr(Object obj) {
        if (SHOW) {
            System.err.print(" -ERROR:" + obj);
            fileIOService.writeToFile(" -ERROR:" + obj.toString());
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
}//end class

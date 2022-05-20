package com.hardcodacii.model;

import java.util.*;

/**
 * @author Sandulache Dumitru
 */
public class  Board {

    private enum CoordinateType {
        //e importanta ordinea in care sunt scrise enumerarile
        ROW, COLUMN, VALUE;
    }
    
    private int numberOfRowsAndColumns = 0;
    private Set<Cell> setOfCell = null;
    private Set<Square> setOfSquare = null;
    private List<List<Cell>> arrayListOfCell = null;

    public int getNumberOfRowsAndColumns() {
        return numberOfRowsAndColumns;
    }
    
    public Set<Cell> getSetOfCell() {
        return setOfCell;
    }
    
    public Set<Square> getSetOfSquare() {
        return setOfSquare;
    }

    public List<List<Cell>> getArrayListOfCell() {
        return arrayListOfCell;
    }
    
    /**
     * Constructor folosit intern.
     */
    private Board() {};
    
    /**
     * Constructor in care se initializeaza grila de joc.
     * @param listFileParsed lista cu valorile luate din fisierul de intrare. Prima valoare din lista e numarul de linii si coloane a patratelor (square) de forma 2x2, 3x3, 4x4.
     * In cazul in care ptr ex 3x3 atunci pentru celule numarul linii=coloane=3^2=9
     */
    public Board(List<Integer> listFileParsed) {
        boolean isFirst = true, makeCell = true; boolean pass = false;
        int count = 1;
        Cell cell = null; Square square;
        
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
                    cell.setIsOriginal(true);
                    
                    count = 1;
                    makeCell = true; pass = false;
                    
                    if (setOfCell == null) setOfCell = new HashSet<>();
                    setOfCell.add(cell);
                }//end if
            }//end else
        }//end for
        
        //se creaza arrayListOfCell
        if (setOfCell != null) {
            //copiaza setOfCell in soc
            Iterator<Cell> iteratorCell = setOfCell.iterator();
            Set<Cell> soc = new HashSet<>();
            while (iteratorCell.hasNext()) soc.add(iteratorCell.next());

            arrayListOfCell = new ArrayList<>();
            List<Cell> listOfCell;
            for (int row = 1; row <= (numberOfRowsAndColumns*numberOfRowsAndColumns); row++) {
                listOfCell = new ArrayList<>();
                arrayListOfCell.add(listOfCell);
                for (int column = 1; column <= (numberOfRowsAndColumns*numberOfRowsAndColumns); column++) {
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
                    Integer rowOfSquare = ((cell.getRow() % numberOfRowsAndColumns) == 0) ? (int) (cell.getRow() / numberOfRowsAndColumns) : (int) (cell.getRow() / numberOfRowsAndColumns) + 1;
                    Integer columnOfSquare = ((cell.getColumn() % numberOfRowsAndColumns) == 0) ? (int) (cell.getColumn() / numberOfRowsAndColumns) : (int) (cell.getColumn() / numberOfRowsAndColumns) + 1;
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
     * Metoda afla o celula in functie de linia si coloana ei.
     * @param row linia celulei
     * @param column coloana celulei
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
                    cellClone.setIsOriginal(cell.getIsOriginal());
                    copyListOfCells.add(cellClone);
                }
                copyArrayListOfCell.add(copyListOfCells);
            }//end while
            boardCopy.arrayListOfCell = copyArrayListOfCell;
        }//end if
        
        return boardCopy;
    }
    
}//end class

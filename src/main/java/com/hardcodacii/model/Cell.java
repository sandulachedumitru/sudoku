package com.hardcodacii.model;

/**
 *
 * @author Sandulache Dumitru
 */
public class Cell extends Element {
    
    private Square squareOfCell;
    private Integer value;
    private boolean isOriginal;
    
    public Cell() {
        super(null, null);
        squareOfCell = null;
        value = null;
        isOriginal = false;
    }
    
    public Cell(Integer row, Integer column) {
        super(row, column);
        squareOfCell = null;
        value = null;
        isOriginal = false;
    }

    public Square getSquareOfCell() {
        return squareOfCell;
    }

    public void setSquareOfCell(Square squareOfCell) {
        this.squareOfCell = squareOfCell;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(boolean isOriginal) {
        this.isOriginal = isOriginal;
    }
    
    
}

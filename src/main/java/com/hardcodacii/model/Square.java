package com.hardcodacii.model;

import java.util.Set;

/**
 *
 * @author Sandulache Dumitru
 */
public class Square extends Element {
    
    private Set<Cell> setOfCell;

    public Square(Integer row, Integer column) {
        super(row, column);
        setOfCell = null;
    }
    
    public Square() {
        super(null, null);
        setOfCell = null;
    }

    public Set<Cell> getSetOfCell() {
        return setOfCell;
    }

    public void setSetOfCell(Set<Cell> setOfCell) {
        this.setOfCell = setOfCell;
    }

}

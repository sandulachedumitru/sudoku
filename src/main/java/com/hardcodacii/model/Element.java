package com.hardcodacii.model;

import java.util.Objects;

/**
 *
 * @author Sandulache Dumitru
 */
public class Element {
    private Integer column, row;
    
    private Element() {}
    
    public Element(Integer row, Integer column) {
        this.column = column;
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 7;
        return (row*PRIME*PRIME + column*PRIME + PRIME) + (column*PRIME*PRIME+row*PRIME+PRIME) + row*column*PRIME + row + column;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Element) {
            Element that = (Element) obj;
            if ((Objects.equals(this.row, that.row)) && (Objects.equals(this.column, that.column))) return true; 
        }
        return false;
    }

}

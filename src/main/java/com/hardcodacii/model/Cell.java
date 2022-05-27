package com.hardcodacii.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Getter
@Setter
public class Cell extends Element {
    
    private Square squareOfCell;
    private Integer value;
    private boolean isOriginal = false;

    public Cell() {
        super(null, null);
    }
    
    public Cell(Integer row, Integer column) {
        super(row, column);
    }
}

package com.hardcodacii.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Getter
@Setter
public class Square extends Element {
    
    private Set<Cell> setOfCell;

    public Square() {
        super(null, null);
    }

    public Square(Integer row, Integer column) {
        super(row, column);
    }
}

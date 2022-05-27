package com.hardcodacii.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@AllArgsConstructor
@Getter
@Setter
public class Element {
    private Integer row, column;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return row.equals(element.row) && column.equals(element.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}

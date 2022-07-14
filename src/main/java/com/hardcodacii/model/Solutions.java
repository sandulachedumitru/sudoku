package com.hardcodacii.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sandulache Dumitru (sandulachedumitru@hotmail.com)
 */

@Getter
@Setter
@Component
public class Solutions {
    private Set<Board> solutions = new HashSet<>();
}

package com.hardcodacii.model;

import java.util.List;

/**
 * @author Sandulache Dumitru
 */
public class Solutions {
    private List<Board> listOfSolutions;

    
//    private Solutions() { };
//    private static class SolutionsHolder {
//        private static Solutions solutions = new Solutions();
//    }
//    public Solutions getInstance() {
//        return SolutionsHolder.solutions;
//    }
//    
    
    public List<Board> getListOfSolutions() {
        return listOfSolutions;
    }
    public void setListOfSolutions(List<Board> listOfSolutions) {
        this.listOfSolutions = listOfSolutions;
    }
}

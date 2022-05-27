package com.hardcodacii.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class TokenizeService {
    private final DisplayService displayService;

    /**
     * 
     * @param fileName is a String that represents the data file
     * @return list of scanned words/numbers
     */
    public List<String> tokenize(String fileName) throws FileNotFoundException {
        boolean isTokenized = false;
        List<String> words = new ArrayList<>();
        try(Scanner tokenizingScanner = new Scanner(new FileReader(fileName))) {
            tokenizingScanner.useDelimiter("\\W");
            while(tokenizingScanner.hasNext()) {
                String word = tokenizingScanner.next();
                if(!word.equals("")) {
                    words.add(word);
                }
            }
            
            int i = 1;
            for(String word : words) {
                displayService.showln(i++ + ": " + word);
            }
            
            isTokenized = true;
        } catch (FileNotFoundException ex) {
            //System.err.println("Cannot read the input file - pass a valid file name");
            //Logger.getLogger(Tokenize.class.getName()).log(Level.SEVERE, null, ex);
            throw new FileNotFoundException("Cannot read the input file - pass a valid file name");
        }
        if (isTokenized) return words;
        else return null;
    }//end method
    
    /**
     * 
     * @param words tokenized list of data file
     * @return only list of numbers of rows/columns/value (coordinate/value) AND total number of rows/columns. 
     *      The list could be null if no args pass the test (In that case variable count == 0 so doesn't matter).
     * @throws IllegalArgumentException if:
     *      Total number of rows/columns smaller than zero
     *      Value of row/column/value biger than total number of rows/columns
     *      One argument missing OR no argument present (0 arguments)
     */
    public List<Integer> analizeTokenizedList(List<String> words) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher;
        
        int numberOfRowsAndColumns = 0;
        boolean isFirst = true;
        List<Integer> listParsed = null;
        
        int tempNo;
        int count = 0;
        
        for (String word : words) {
            matcher = pattern.matcher(word);
            if (matcher.find()) {
                if (isFirst) {
                    numberOfRowsAndColumns = Integer.parseInt(word);
                    if (numberOfRowsAndColumns <= 0) {
                        throw new IllegalArgumentException("Total number of rows/columns equal with zero ( Total:" + numberOfRowsAndColumns + " ).");                      
                    }
                    
                    listParsed = new ArrayList<>();                    
                    listParsed.add(numberOfRowsAndColumns);
                    isFirst = false;
                    count++;
                } else {
                    tempNo = Integer.parseInt(word);
                    if (tempNo > numberOfRowsAndColumns*numberOfRowsAndColumns) {
                        System.err.println("Value of row/column/value biger than total number of rows/columns.");
                        throw new IllegalArgumentException("Value of row/column biger than total number of rows/columns (" +
                                tempNo + " > " + numberOfRowsAndColumns*numberOfRowsAndColumns +
                                ").");
                    } else {
                        listParsed.add(tempNo);
                        count++;
                    }//end else
                }//end else
            }//end if
        }//end for
        
        
        
        if ((((count - 1) % 3) != 0) || (count == 0)) throw new IllegalArgumentException("Least one argument missing OR no argument present (0 arguments).");
        else {
            int i = 1;
            for(Integer lp : listParsed) {
                displayService.showln(i++ + ": " + lp);
            }
            return listParsed;
        }
    }//end method
}//end T

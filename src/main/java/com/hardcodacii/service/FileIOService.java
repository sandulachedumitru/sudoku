package com.hardcodacii.service;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

public interface FileIOService {
    boolean exists(String path);
    void writeToFile (String str);
}

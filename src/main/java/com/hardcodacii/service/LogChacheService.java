package com.hardcodacii.service;

import org.springframework.stereotype.Component;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@Component
public class LogChacheService {
    private String CRLF = "\r\n";
    private String ERROR = " -ERROR:";

    private StringBuilder logChache = new StringBuilder();

    public void showln(Object obj) {
        logChache.append(obj.toString());
        logChache.append(CRLF);
    }

    public void show(Object obj) {
        logChache.append(obj.toString());
    }

    public void showlnErr(Object obj) {
        logChache.append(ERROR + obj.toString());
        logChache.append(CRLF);
    }

    public void showErr(Object obj) {
        logChache.append(ERROR + obj.toString());
    }

    public StringBuilder getLogChache() {
        return logChache;
    }

    public String getErrorSuffix() {
        return ERROR;
    }
}

package com.hardcodacii.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class FileIOServiceImpl {
    private static final String resultFile = "SudokuResults.txt";
    private static BufferedWriter outputFile;

    private final DisplayService displayService;

    public boolean exists(String path) {
        boolean fileExists = false;

        Path pathSource = Paths.get(path);
        if (Files.exists(pathSource, LinkOption.NOFOLLOW_LINKS)) {
            displayService.showln("The file/directory \"" + pathSource.getFileName() + "\" exists");
            // check whether it is a file or a directory
            if (Files.isDirectory(pathSource, LinkOption.NOFOLLOW_LINKS)) {
                displayService.showlnErr("\"" + pathSource.getFileName() + "\" is a directory. Need a file.");
                //if directory then teminate app
            } else {
                displayService.showln("\"" + pathSource.getFileName() + "\" is a file");
                fileExists = true;
            }
        } else
            displayService.showlnErr("The file \"" + pathSource.getFileName() + "\" does not exist");

        return fileExists;
    }

    public void writeToFile(String str) {
        try {
            outputFile.write(str);
            outputFile.flush();
            outputFile.close();
        } catch (IOException ex) {
            System.err.println("Error when processing file in method writeToFile(); exiting ... ");
            System.err.println(ex);
        }
    }
}

package com.hardcodacii.view;

import java.nio.file.*;

import static com.hardcodacii.view.Show.showln;
import static com.hardcodacii.view.Show.showlnErr;

/**
 *
 * @author Sandulache Dumitru
 * 
 */
public class FileExists {
    /**
     * 
     * @param path of type String (path of data file)
     * @return boolean true if file exist or false if file do not exist or path is a directory
     * 
     */
    public boolean exists(String path) {
        boolean fileExists = false;
	Path pathSource = Paths.get(path);
	if(Files.exists(pathSource, LinkOption.NOFOLLOW_LINKS)) {
            showln("The file/directory \"" + pathSource.getFileName() + "\" exists");			
            // check whether it is a file or a directory
            if(Files.isDirectory(pathSource, LinkOption.NOFOLLOW_LINKS)) {
		showlnErr("\"" + pathSource.getFileName() + "\" is a directory. Need a file.");
		//if directory then teminate app
            }
            else {
		showln("\"" + pathSource.getFileName() + "\" is a file");
                fileExists = true;
            }
	}
	else {
            showlnErr("The file \"" + pathSource.getFileName() + "\" does not exist");
	}
        
        return fileExists;
    }//end method
}//end class

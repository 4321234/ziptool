package pl.mir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class LOGGER {
	
	private String file_name;
	
    public LOGGER(String fileName) throws Exception {
    	file_name = fileName;
    	create_log_file();
    }
    private void create_log_file() throws Exception {
    	File log = new File(System.getProperty("user.dir") + File.separator + file_name + ".log");
    	log.createNewFile();
    }
    
    public void add(String txt) throws Exception {
    	txt = txt + System.lineSeparator();
    	Files.write(Paths.get(file_name + ".log"), txt.getBytes(), StandardOpenOption.APPEND);
    	
    }

}

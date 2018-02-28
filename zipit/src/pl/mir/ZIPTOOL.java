package pl.mir;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZIPTOOL {
	
    private List <String> fileList;
    public static String OUTPUT_ZIP_FILE;
    public static String SOURCE_FOLDER; // SourceFolder path
    private static String FILE_NAME;
    
    public ZIPTOOL() throws Throwable {
        fileList = new ArrayList < String > ();
        FILE_NAME = createArchiveName();
        
    }
    
	public static void main(String[] args) throws Throwable {

		ZIPTOOL appZip = new ZIPTOOL();
		LOGGER log = new LOGGER(FILE_NAME);
		
		SOURCE_FOLDER = args[0];
		OUTPUT_ZIP_FILE = args[1];
		
        appZip.generateFileList(new File(SOURCE_FOLDER));
        appZip.zipIt(OUTPUT_ZIP_FILE + File.separator + FILE_NAME + ".zip", log);
        clearDirectory(SOURCE_FOLDER, log);
        createDirectory(SOURCE_FOLDER);

	}
	
	public void zipIt(String zipFile, LOGGER log) throws Exception {
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);
            log.add("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file: this.fileList) {
                System.out.println("File Added : " + file);
                log.add("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");
            log.add("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }
    
    public static String createArchiveName() throws Throwable {
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    	Date date = new Date();
    	System.out.println(dateFormat.format(date));    	
    	String archiveName = InetAddress.getLocalHost().getHostName() + "_" + dateFormat.format(date);
    	
    	return archiveName;
    }
    
    public static void createDirectory(String SRC_FOLDER) {
    	
    	File dir = new File(SRC_FOLDER);
    	dir.mkdir();
    	
    }
    
    public static void clearDirectory(String SRC_FOLDER, LOGGER log) throws Exception {
    	
    	File directory = new File(SRC_FOLDER);

    	//make sure directory exists
    	if(!directory.exists()){

           System.out.println("Directory does not exist.");
           log.add("Directory " + SRC_FOLDER + " does not exist.");
           System.exit(0);

        }else{

           try{

               delete(directory,log);

           }catch(IOException e){
               e.printStackTrace();
               System.exit(0);
           }
        }

    	System.out.println("Done");
    	log.add("Done");
    	
    }
    
    
    public static void delete(File file, LOGGER log)
        	throws Exception{

        	if(file.isDirectory()){

        		//directory is empty, then delete it
        		if(file.list().length==0){

        		   file.delete();
        		   System.out.println("Directory is deleted : "
                                                     + file.getAbsolutePath());
        		   log.add("Directory is deleted : "
                                                     + file.getAbsolutePath());

        		}else{

        		   //list all the directory contents
            	   String files[] = file.list();

            	   for (String temp : files) {
            	      //construct the file structure
            	      File fileDelete = new File(file, temp);

            	      //recursive delete
            	     delete(fileDelete, log);
            	   }

            	   //check the directory again, if empty then delete it
            	   if(file.list().length==0){
            		 System.out.println("Last Modified: " + file.lastModified());
            		 log.add("Last Modified: " + file.lastModified());
               	     file.delete();
            	     System.out.println("Directory is deleted : "
                                                      + file.getAbsolutePath());
            	     log.add("Directory is deleted : "
                                                      + file.getAbsolutePath());
            	   }
        		}

        	}else{
        		//if file, then delete it
        		file.delete();
        		System.out.println("File is deleted : " + file.getAbsolutePath());
        		log.add("File is deleted : " + file.getAbsolutePath());
        	}
        }

}

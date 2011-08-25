package controllers;

import play.*;
import play.mvc.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.commons.lang.ArrayUtils;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void content(String dir) {
        String[] fileNames = new String[0];
        String[] dirNames = new String[0];
        String[] files = new String[0];
        final String BASE_DIR = Play.applicationPath.getAbsolutePath() + "/public";
        String full_dir = "";
        
        if (dir == null || dir.equals("")) {
            dir = "/";
        }
        
        dir = dir.replaceAll("\\.\\.", "");
        
        if (dir != null) {
            if (dir.charAt(dir.length()-1) == '\\') {
                dir = dir.substring(0, dir.length()-1) + "/";
            } else if (dir.charAt(dir.length()-1) != '/') {
                dir += "/";
            }
            
            try {
                dir = java.net.URLDecoder.decode(dir, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            full_dir = BASE_DIR + dir;

            if (new File(full_dir).exists()) {
                files = new File(full_dir).list(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.charAt(0) != '.';
                    }
                });

                Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
                
                // All folders
                for (String file : files) {
                    if (new File(full_dir, file).isDirectory()) {
                        dirNames = (String[]) ArrayUtils.add(dirNames, dir + file);                        
                    }
                }
                
                // All files
                for (String file : files) {
                    if (!new File(full_dir, file).isDirectory()) {
                        fileNames = (String[]) ArrayUtils.add(fileNames, dir + file);
                    }
                }
            }
        }
        
        render(dirNames, fileNames);        
    }
}

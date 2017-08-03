package com.genlan.veertest.util;

import com.genlan.veertest.MyApplication;

import java.io.File;
import java.io.IOException;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class FileUtil {

    private FileUtil() {
        throw new RuntimeException("Don't let anyone instantiate this class");
    }

    /**
     * @param path parent path of file
     * @param name name of file
     * @return created
     */
    public static boolean createFile(String path, String name) {
        return false;
    }

    /**
     * create a file on app path
     *
     * @param name name of file
     * @return created
     */
    public static boolean createFile(String name) throws IOException {
        File temp = MyApplication.getInstance().getFilesDir();
        File file = new File(temp, name);
        return file.createNewFile();
    }

}

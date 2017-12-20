package bwie.com.basemodule;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by 李英杰 on 2017/12/11.
 * Description：
 */

public class FileUtils {

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    public static String getFormatSize(double fileSize) {
        DecimalFormat decimalFormat=new DecimalFormat("#.00");
        String size="";
        if (fileSize<1024){
            size=decimalFormat.format((double) fileSize)+"B";
        }else if (fileSize<1048576){
            size=decimalFormat.format((double) fileSize/1024)+"KB";
        }else if (fileSize<1073741824){
            size=decimalFormat.format((double) fileSize/1048576)+"MB";
        }else {
            size=decimalFormat.format((double) fileSize/ 1073741824)+"G";
        }
        return size;
    }

    public static void delete(File file){
        if (file==null){
            return;
        }
        if (!file.exists()){
            return;
        }
        if (file.isFile()){
            return;
        }
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isFile()){
                file1.delete();
            }else if (file1.isDirectory()){
                delete(file1);
            }
        }
    }

}

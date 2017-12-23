package bwie.com.basemodule;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * Created by 李英杰 on 2017/12/11.
 * Description：
 */

public class FileUtils {

    /**
     * 获取文件大小
     * @param file
     * @return
     * @throws Exception
     */
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


    /**
     *  格式转换
     * @param fileSize
     * @return
     */
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

    /**
     *  删除文件
     * @param file
     */
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

    /**
     *  文件加密
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file){

        String value=null;
        FileInputStream in=null;
        try {
            in=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            MappedByteBuffer map = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(map);
            BigInteger bigInteger=new BigInteger(1,messageDigest.digest());
            value=bigInteger.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

}

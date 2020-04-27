package com.hsbc.upload.fileupload.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 解压Zip文件工具类
 * Created by like on 2017年9月27日
 *
 */
public class ZipUtil
{
    private static final int buffer = 2048;
    /**
     * 解压Zip文件
     * @param path 文件目录
     */
    public static void unZip(String path)
    {
        int count = -1;
        String savepath = "";
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        savepath = path.substring(0, path.lastIndexOf(".")) + File.separator; //保存解压文件目录
        new File(savepath).mkdir(); //创建保存目录
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(path,"gbk"); //解决中文乱码问题
            Enumeration<?> entries = zipFile.getEntries();
            while(entries.hasMoreElements())
            {
                byte buf[] = new byte[buffer];
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String filename = entry.getName();
                boolean ismkdir = false;
                if(filename.lastIndexOf("/") != -1){ //检查此文件是否带有文件夹
                    ismkdir = true;
                }
                filename = savepath + filename;
                if(entry.isDirectory()){ //如果是文件夹先创建
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                if(!file.exists()){ //如果是目录先创建
                    if(ismkdir){
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                    }
                }
                file.createNewFile(); //创建文件
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, buffer);
                while((count = is.read(buf)) > -1)
                {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();
                is.close();
            }
            zipFile.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null) {
                    fos.close();
                }
                if(is != null){
                    is.close();
                }
                if(zipFile != null){
                    zipFile.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param fileList ：多个文件的path+name
     * @param zipFileName ：压缩后的文件名
     * @return
     */
    public static String createZipFile(ArrayList<String> fileList, String zipFileName) {

        if(fileList == null || fileList.size() == 0 || StringUtils.isEmpty(zipFileName)){
            return null;
        }

        //构建压缩文件File
        File zipFile = new File(zipFileName);
        //初期化ZIP流
        ZipOutputStream out = null;

        try{
            //构建ZIP流对象
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            //循环处理传过来的集合
            for(int i = 0; i < fileList.size(); i++){
                //获取目标文件
                File inFile = new File(fileList.get(i));
                if(inFile.exists()){
                    //定义ZipEntry对象
                    ZipEntry entry = new ZipEntry(inFile.getName());
                    //赋予ZIP流对象属性
                    out.putNextEntry(entry);
                    int len = 0 ;
                    //缓冲
                    byte[] buffer = new byte[1024];
                    //构建FileInputStream流对象
                    FileInputStream fis;
                    fis = new FileInputStream(inFile);
                    while ((len = fis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    //关闭closeEntry
                    out.closeEntry();
                    //关闭FileInputStream
                    fis.close();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                //最后关闭ZIP流
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return zipFileName;

    }
}
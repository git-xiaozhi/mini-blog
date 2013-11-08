package com.xiaozhi.blog.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

public class FileUtil {

    private static Log logger = LogFactory.getLog(FileUtil.class);

    public static String getExtension(File f) {
        return (f != null) ? getExtension(f.getName()) : "";
    }

    public static String getExtension(String filename) {
        return getExtension(filename, "");
    }

    public static String getExtension(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i >-1) && (i < (filename.length() - 1))) {
                return filename.substring(i + 1);
            }
        }
        return defExt;
    }

    public static String trimExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if ((i >-1) && (i < (filename.length()))) {
                return filename.substring(0, i);
            }
        }
        return filename;
    }
    
    /**
     * 微博图片按宽度比例压缩
     * @param a
     * @param filePath
     * @param uid
     * @param filename
     * @param width
     * @param isBig
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String uploadFileHandle(byte[] a,String filePath,String uid,String filename,int width) throws IOException, InterruptedException{
        if(logger.isDebugEnabled()){

            logger.debug("------------------------->"+filePath+File.separator+uid);
        }
        File dir=new File(filePath+File.separator+uid);
        if(!dir.exists())dir.mkdir();



        long time = System.currentTimeMillis();
        //生成大图片
        int bigWidth=width;
        if(width>400)bigWidth=400;
        String returnfileName = time+"_large."+getExtension(filename);
        String path=filePath+File.separator+uid+File.separator+returnfileName;
        byte[] big = Im4javaUitl.resiizeImage(bigWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(big, new File(path));

        //生成小图片
        int smallWidth=width;
        if(width>150)smallWidth=150;
        String smallfileName = time+"."+getExtension(filename);
        String smallpath=filePath+File.separator+uid+File.separator+smallfileName;
        byte[] small = Im4javaUitl.resiizeImage(smallWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(small, new File(smallpath));

        return uid+"/"+smallfileName;
    }



    
    /**
     * 临时保存sina上传图片，不对图片进行处理
     * @param a
     * @param filePath
     * @param uid
     * @param filename
     * @param width
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
      public static String sinaUploadFileHandle(byte[] a,String filePath,String uid,String filename) throws IOException, InterruptedException{
          if(logger.isDebugEnabled()){

              logger.debug("------------------------->"+filePath+File.separator+uid);
          }
          File dir=new File(filePath+File.separator+uid);
          if(!dir.exists())dir.mkdir();

          
          String returnfileName = "sina_temp."+getExtension(filename);
          String path=filePath+File.separator+uid+File.separator+returnfileName;
          FileCopyUtils.copy(a, new File(path));

          return uid+"/"+returnfileName;
      }
      
      
      /**
       * 定时发送功能上传图片
       * @param a
       * @param filePath
       * @param uid
       * @param filename
       * @return
       * @throws IOException
       * @throws InterruptedException
       */
      public static String sinaUploadFile(byte[] a,String filePath,String uid,String filename) throws IOException, InterruptedException{
          File dir=new File(filePath+File.separator+uid);
          if(!dir.exists())dir.mkdirs();

          long time = System.currentTimeMillis();
          String returnfileName = time+"."+getExtension(filename);
          String path=filePath+File.separator+uid+File.separator+returnfileName;
          FileCopyUtils.copy(a, new File(path));

          return uid+"/"+returnfileName;
      }


    /**
     * 获取图片宽度尺寸
     * @param a
     * @return
     */
    public static int getImageWidth(InputStream is){

        try {
            BufferedImage sourceImg = javax.imageio.ImageIO.read(is);
            return sourceImg.getWidth();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0;
    }


	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}



}

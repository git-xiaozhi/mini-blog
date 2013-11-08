package com.xiaozhi.image;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.xiaozhi.blog.img.ImageService;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.utils.Im4javaUitl;





@Service
public class ImageServiceImpl implements ImageService {

    private static Log logger = LogFactory.getLog(ImageServiceImpl.class);
    
    @Value(value = "#{globalProperties['images.filePath']}")
    private String filePath;
    
    @Value(value = "#{globalProperties['images.imageUrl']}")
    private String imageUrl;




    /* (non-Javadoc)
	 * @see com.xiaozhi.image.ImageService#uploadOriginalFileHandle(byte[], java.lang.String, java.lang.String, int)
	 */
    @Override
	public String uploadOriginalFileHandle(byte[] a,String uid,String filename,int width)
			throws IOException, InterruptedException{

        File dir=new File(this.filePath+File.separator+uid);
        if(logger.isDebugEnabled()){
            logger.debug("------------------------->dir :"+dir);
        }
        
        if(!dir.exists()){
        	try {
        		dir.mkdirs();
			} catch (Exception e) {
				System.out.println("------------------------->error :"+e);
			}
        	
        }

        //原始图片处理
        int bigWidth=width;
        if(width>1000)bigWidth=1000;
        String returnfileName = uid+"_original."+FileUtil.getExtension(filename);
        String path=this.filePath+File.separator+uid+File.separator+returnfileName;
        byte[] big = Im4javaUitl.resiizeImage(bigWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(big, new File(path));

        return this.imageUrl+uid+"/"+returnfileName;
    }




    /* (non-Javadoc)
	 * @see com.xiaozhi.image.ImageService#ProHandle(java.lang.String, java.lang.String, int, int, int, int)
	 */
    @Override
	public String ProHandle(String uid,String filename,int width,int heigth,int x,int y)
			throws IOException, InterruptedException{


        byte[] a = FileCopyUtils.copyToByteArray(new File(this.filePath+File.separator+uid+File.separator+filename));

        /**头像裁剪没有选择域则不裁剪直接压缩*/
        if(width!=0 || heigth!=0)a = Im4javaUitl.cropImage(width, heigth,x,y,a);


        /**头像压缩成大小2张图片*/
        //生成大图片
        int bigWidth=width;
        if(width>150)bigWidth=150;
        String largefileName = uid+"_large."+FileUtil.getExtension(filename);
        String path=this.filePath+File.separator+uid+File.separator+largefileName;

        byte[] big = Im4javaUitl.resiizeImage(bigWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(big, new File(path));

        //生成小图片
        int smallWidth=width;
        if(width>50)smallWidth=50;
        String smallfileName = uid+"."+FileUtil.getExtension(filename);
        String smallpath=this.filePath+File.separator+uid+File.separator+smallfileName;
        byte[] small = Im4javaUitl.resiizeImage(smallWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(small, new File(smallpath));

        return smallfileName;
    }
    
    
    
    /**
     * 微博图片按宽度比例压缩
     */
    public String uploadFileHandle(byte[] a,String uid,String filename,int width) throws IOException, InterruptedException{


        long time = System.currentTimeMillis();
        //生成大图片
        int bigWidth=width;
        if(width>400)bigWidth=400;
        String returnfileName = time+"_large."+FileUtil.getExtension(filename);
        String path=this.filePath+File.separator+uid+File.separator+returnfileName;
        byte[] big = Im4javaUitl.resiizeImage(bigWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(big, new File(path));

        //生成小图片
        int smallWidth=width;
        if(width>150)smallWidth=150;
        String smallfileName = time+"."+FileUtil.getExtension(filename);
        String smallpath=this.filePath+File.separator+uid+File.separator+smallfileName;
        byte[] small = Im4javaUitl.resiizeImage(smallWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(small, new File(smallpath));

        return this.imageUrl+uid+"/"+smallfileName;
    }

}

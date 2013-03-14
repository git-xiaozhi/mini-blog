package com.xiaozhi.blog.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.stereotype.Service;



@Service
public class Im4javaUitl {

	private static final  Log logger = LogFactory.getLog(Im4javaUitl.class);
	
	/**
	 * 文件对文件压缩
	 * @param width
	 * @param height
	 * @param srcPath
	 * @param desPath
	 * @return
	 */
	public static boolean resiizeImage(Integer width,Integer height,String srcPath,String desPath) {

		IMOperation op = new IMOperation();
	    op.addImage(); //place holder for input file
	    op.resize(height,width);
	    op.addImage(); //place holder for output file
	    
	    ForWinConvertCmd convert = new ForWinConvertCmd();
	    try {
			convert.run(op,srcPath,desPath);
	    } catch (IOException e) {
	    	logger.error(e.getMessage());
	    	return false;
	    } catch (InterruptedException e) {
	    	logger.error(e.getMessage());
	    	return false;
		} catch (IM4JavaException e) {
			logger.error(e.getMessage());
	    	return false;
		}

		return true;
	}

	
	/**
	 * 压缩stream流to btye流
	 * @param height
	 * @param width
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static byte[] resiizeImage(Integer height, Integer width, InputStream stream)throws IOException, InterruptedException {

		IMOperation op = new IMOperation();
		op.addImage("-");
        op.resize(height,width);
		op.addImage("-");
		Pipe pipeIn = new Pipe(stream, null);
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		Pipe pipeOut = new Pipe(null, fos);

		// set up command
		ForWinConvertCmd convert = new ForWinConvertCmd();//兼容windows操作系统
		convert.setInputProvider(pipeIn);
		convert.setOutputConsumer(pipeOut);
		try {
			convert.run(op);
		} catch (IM4JavaException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
			stream.close();
		}
		return fos.toByteArray();
	}
	

	/**
	 * btye流tobtye流压缩
	 * @param height
	 * @param width
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] resiizeImage(Integer height, Integer width, byte[] b)throws IOException, InterruptedException {

		
		IMOperation op = new IMOperation();
		op.addImage("-");
		op.resize(height,width);
		op.addImage("-");
		
		ByteArrayInputStream sbs = new ByteArrayInputStream(b);
		Pipe pipeIn = new Pipe(sbs, null);

		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		Pipe pipeOut = new Pipe(null, fos);

		// set up command
		ForWinConvertCmd convert = new ForWinConvertCmd();
		convert.setInputProvider(pipeIn);
		convert.setOutputConsumer(pipeOut);

		try {
			convert.run(op);
		} catch (IM4JavaException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
			sbs.close();
		}
		return fos.toByteArray();
	}
	

	/**
	 * 图片裁剪不压缩
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] cropImage(Integer width,Integer height ,int x,int y, byte[] b)throws IOException, InterruptedException {
		
		IMOperation op = new IMOperation();
		op.addImage("-");
		op.append().crop(width, height, x, y);
		op.addImage("-");
		
		ByteArrayInputStream sbs = new ByteArrayInputStream(b);
		Pipe pipeIn = new Pipe(sbs, null);

		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		Pipe pipeOut = new Pipe(null, fos);

		// set up command
		ForWinConvertCmd convert = new ForWinConvertCmd();
		convert.setInputProvider(pipeIn);
		convert.setOutputConsumer(pipeOut);

		try {
			convert.run(op);
		} catch (IM4JavaException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
			sbs.close();
		}
		return fos.toByteArray();
	}

}

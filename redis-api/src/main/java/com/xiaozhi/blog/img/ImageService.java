package com.xiaozhi.blog.img;

import java.io.IOException;

public interface ImageService {

	/**
	 * 头像原始图片上传处理
	 * 
	 * @param a
	 * @param uid
	 * @param filename
	 * @param width
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract String uploadOriginalFileHandle(byte[] a, String uid,
			String filename, int width) throws IOException,
			InterruptedException;

	/**
	 * 头像裁剪，并生成大小两个图片
	 * 
	 * @param a
	 * @param uid
	 * @param filename
	 *            原始图片文件名
	 * @param width
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract String ProHandle(String uid, String filename, int width,
			int heigth, int x, int y) throws IOException, InterruptedException;

	/**
	 * 微博图片按宽度比例压缩
	 * @param a
	 * @param uid
	 * @param filename
	 * @param width
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract String uploadFileHandle(byte[] a, String uid,
			String filename, int width) throws IOException,
			InterruptedException;

}
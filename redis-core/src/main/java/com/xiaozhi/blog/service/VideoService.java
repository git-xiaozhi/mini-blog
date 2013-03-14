package com.xiaozhi.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.service.video.VideoHandler;
import com.xiaozhi.blog.vo.Video;

@Service
public class VideoService {
	
	@Autowired
	private VideoHandler ku6VideoHandler;
	@Autowired
	private VideoHandler fiftySixVideoHandler;
	@Autowired
	private VideoHandler sixVideoHandler;
	@Autowired
	private VideoHandler tudouVideoHandler;
	@Autowired
	private VideoHandler youKuVideoHandler;
	
	
	
	
	public Video getVideoInfo(String url){
		
		ku6VideoHandler.setSuccessor(fiftySixVideoHandler);
		fiftySixVideoHandler.setSuccessor(sixVideoHandler);
		sixVideoHandler.setSuccessor(tudouVideoHandler);
		tudouVideoHandler.setSuccessor(youKuVideoHandler);
		
		Video video = ku6VideoHandler.getVideo(url);
		video.setHtmlpath(url);
		return video;
	}

}

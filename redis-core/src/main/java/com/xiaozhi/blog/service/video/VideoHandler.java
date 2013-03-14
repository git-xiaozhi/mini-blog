package com.xiaozhi.blog.service.video;

import org.springframework.stereotype.Service;

import com.xiaozhi.blog.vo.Video;

@Service
public abstract class VideoHandler  {

	protected VideoHandler successor;
	
	
	public abstract Video  getVideo(String url);

	
	public void setSuccessor(VideoHandler successor) {
		this.successor = successor;
	}
	
}

package com.xiaozhi.blog.service.other.sina;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.web.others.sina.CommentShow;


import weibo4j.Timeline;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Emotion;
import weibo4j.model.Favorites;
import weibo4j.model.FavoritesWapper;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;


@Service
public class BlogTime{


	private static final long serialVersionUID = 1667045951680582897L;
	private static Log logger = LogFactory.getLog(BlogTime.class);

	private static final Pattern FACE_REGEX = Pattern.compile("\\[([^\\]\\[\\/ ]+)\\]");

	private static Map<String, Emotion> emotionMap = null;
	private static List<Faces> emotionList = null;

	@Autowired
	private Timeline timeline;

    /**
     * 新浪表情获取封装
     * @param accessToken
     * @return
     * @throws WeiboException
     */
    public List<Faces> getRemoteExp(String accessToken) throws WeiboException{
    	if(emotionList!=null)return emotionList;
    	timeline.client.setToken(accessToken);
    	List<Emotion> meotions=timeline.getEmotions();
    	ArrayListMultimap<String,Icon> multiMap = ArrayListMultimap.create();
    	Map<String,String> names = new LinkedHashMap<String,String>();
    	for(Emotion emotion :meotions){
    		names.put(emotion.getCategory(),emotion.getCategory());
    		multiMap.put(emotion.getCategory(), new Icon(emotion.getUrl(),StringUtils.substringBetween(emotion.getPhrase(), "[", "]")));
    	}

    	List<Faces> faces=new ArrayList<Faces>(names.size());

        for (Iterator<String> iterator = names.values().iterator(); iterator.hasNext();) {
            String name = iterator.next();
            faces.add(new Faces(name,multiMap.get(name)));
        }
        emotionList = faces;
    	return emotionList;
    }


    /**
     * 新浪微博表情显示封装
     * @param statusWapper
     * @param timeline
     * @return
     * @throws WeiboException
     */
	public StatusWapper getStatusWapperWithFaceImage(StatusWapper statusWapper,Timeline timeline) throws WeiboException{
		Map<String, Emotion> map = getEmotionsMap(timeline);
		List<Status> statuses = statusWapper.getStatuses();
		for(Status status:statuses){
			status.setText(replayFaceImages(status.getText(),map));
			if(status.getRetweetedStatus()!=null){
				Status 	retweetedStatus = status.getRetweetedStatus();
				retweetedStatus.setText(replayFaceImages(retweetedStatus.getText(),map));
			}
		}
		return statusWapper;
	}

    /**
     * 我的收藏表情图标显示封装
     * @param favoritesWapper
     * @param timeline
     * @return
     * @throws WeiboException
     */
	public FavoritesWapper getFavoritesWapperWithFaceImage(FavoritesWapper favoritesWapper,Timeline timeline) throws WeiboException{
		Map<String, Emotion> map = getEmotionsMap(timeline);
		List<Favorites> favoritesList = favoritesWapper.getFavoriteslist();
		for(Favorites f:favoritesList){
			f.getStatus().setText(replayFaceImages(f.getStatus().getText(),map));
			if(f.getStatus().getRetweetedStatus()!=null){
				Status 	retweetedStatus = f.getStatus().getRetweetedStatus();
				retweetedStatus.setText(replayFaceImages(retweetedStatus.getText(),map));
			}
		}
		return favoritesWapper;
	}

    /**
     * 替换评论列表表情
     * @param commentWapper
     * @param accessToken
     * @return
     * @throws WeiboException
     */
	public List<Comment> getCommentWapperWithFaceImage(CommentWapper commentWapper,SinaAccessToken accessToken) throws WeiboException{
		timeline.client.setToken(accessToken.getAccesstoken());
		List<Comment> commentList = new ArrayList<Comment>();
		Map<String, Emotion> map = getEmotionsMap(timeline);
		List<Comment> comments = commentWapper.getComments();
		for(Comment c:comments){
			c.setText(replayFaceImages(c.getText(),map));

			CommentShow commentShow = new CommentShow();
			BeanUtils.copyProperties(c, commentShow, new String[]{"canDelete"});
			commentShow.setCanDelete(commentShow.getUser().getId().equals(accessToken.getUserid())?true:false);
			commentList.add(commentShow);
		}
		return commentList;
	}
	
	/**
	 * 替换一条评论表情
	 * @param comment
	 * @param accessToken
	 * @return
	 * @throws WeiboException
	 */
	public Comment getCommentWithFaceImage(Comment comment,SinaAccessToken accessToken) throws WeiboException{
		timeline.client.setToken(accessToken.getAccesstoken());
		Map<String, Emotion> map = getEmotionsMap(timeline);
		comment.setText(replayFaceImages(comment.getText(),map));
		return comment;
	}

    /**
     * 替换发布内容的表情符号
     * @param resource
     * @return
     * @throws WeiboException
     */
    public  String replayFaceImages(String resource,Map<String, Emotion> map) throws WeiboException{
        Matcher regexMatcher = FACE_REGEX.matcher(resource);
        while (regexMatcher.find()) {
            String match = regexMatcher.group();
            if(map.get(match)!=null){
             String imageurl = "<img src='"+map.get(match).getUrl()+"'/>";
             resource = resource.replace(match, imageurl);
            }
        }
        return resource;
    }



	private Map<String, Emotion> getEmotionsMap(Timeline timeline) throws WeiboException {
		if(emotionMap!=null)return emotionMap;
    	if(logger.isDebugEnabled())logger.debug("------------------------------> 从新浪api获取！");
		List<Emotion> meotions=timeline.getEmotions();
	    Map<String, Emotion> map = new ConcurrentHashMap<String, Emotion>(meotions.size());
		for(Emotion mEmotion :meotions){
			map.put(mEmotion.getPhrase(), mEmotion);
		}
		emotionMap = map;
		return emotionMap;
	}


}

package com.xiaozhi.blog.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.vo.User;




@Component
public class MentionUtil {

     private static Log logger = LogFactory.getLog(MentionUtil.class);

    private static final Pattern MENTION_REGEX = Pattern.compile("@([-_a-zA-Z0-9\u4e00-\u9fa5]{2,20}+)");
    private static final Pattern FACE_REGEX = Pattern.compile("\\[([^\\]\\[\\/ ]+)\\]");
    private static final int OFFSET = 19;

    @Autowired
    private MongoUserDao mongoUserDao;

    @Autowired
    private MessageSource messageSource;



    /**
     * 替换发布内容的表情符号
     * @param resource
     * @return
     */
    public  String replayFaceImages(String resource){
        Matcher regexMatcher = FACE_REGEX.matcher(resource);
        while (regexMatcher.find()) {
            String match = regexMatcher.group();
            String imageurl = "<img src='"+this.messageSource.getMessage(match, null,null)+"'/>";
            if(logger.isDebugEnabled()){
                logger.debug("------------------------>imageurl :"+ imageurl);
            }
            resource = resource.replace(match, imageurl);
        }
        return resource;
    }


    /**
     * 查找微薄内容中所提及的人
     * @param content
     * @return
     */
    public  List<String> findMentions(String content) {
        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        List<String> mentions = new ArrayList<String>();

        while (regexMatcher.find()) {
            mentions.add(regexMatcher.group().substring(1));
        }

        return mentions;
    }


    /**
     * 为提及的人加链接
     * @param content
     * @return
     */
    public String replaceMentions(String content) {
        List<Macher> machers=new ArrayList<Macher>();
        List<String> nicknames=new ArrayList<String>();
        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        while (regexMatcher.find()) {
            Macher macher = new Macher();
            String match = regexMatcher.group();
            macher.setStart(regexMatcher.start());
            macher.setStop(regexMatcher.end());
            macher.setuName(match.substring(1));
            machers.add(macher);
            nicknames.add(macher.getuName());
        }

        Map<String, User> map = this.mongoUserDao.getUsersByNickNames(nicknames);
        int i=0;
        for(Macher matcher :machers){
        	//判断昵称是否存在
        	User user = map.get(matcher.getuName());
            if (user!=null) {
                content = content.substring(0, matcher.getStart()+OFFSET*i) + "<a href=\"/u/" + user.getId() + "\">@" + matcher.getuName() + "</a>"
                        + content.substring(matcher.getStop()+OFFSET*i);

            }
            i++;
        }
        if(logger.isDebugEnabled())logger.debug("---------------------->content :"+content);
        return content;
    }


    public class Macher{

      private  int start;
      private  int stop;
      private  String uName;

    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getStop() {
        return stop;
    }
    public void setStop(int stop) {
        this.stop = stop;
    }
    public String getuName() {
        return uName;
    }
    public void setuName(String uName) {
        this.uName = uName;
    }



   }

}

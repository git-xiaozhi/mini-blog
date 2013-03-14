package com.xiaozhi.blog.oauth2client.sina;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.vo.SinaAccessToken;



@Controller
public class Oauth2CodeSina {

	@Autowired
	private  MongoUserDao mongoUserDao;

	private static Log logger = LogFactory.getLog(Oauth2CodeSina.class);

	private static final String clientId = "300873377";
	private static final String clientSecret = "53ccbe77c53472c7ec14ae2b53ba6ff7";

	private static final String oauthBase = "https://api.weibo.com/oauth2";
	private static final String authorizeURL = oauthBase + "/authorize";
	private static final String accessTokenURL = oauthBase + "/access_token";
	//private static final String apiBase = "https://api.tianji.com";


	/**
	 * 帐号绑定
	 * @param page
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/bind/sina", method = RequestMethod.GET)
	public String bind(HttpServletRequest request, @RequestParam(required = false) String callbackUrl,Model model) throws IOException {

		String code = request.getParameter("code");
		String redirectUri = request.getRequestURL().toString();
		if(logger.isDebugEnabled())	{
			   logger.debug("---------------------->code :"+code);
			   logger.debug("---------------------->redirectUri :"+redirectUri);
		}
		if (code == null || "".equals(code)) {

			// Step 1 - Redirect user to provider for authorization
			String url = authorizeURL + "?client_id="+ clientId + "&redirect_uri=" + redirectUri;
			//response.sendRedirect(url);
			return "redirect:"+url;

		} else {

			// Step 2 - Exchange for access grant
			String urlParameters = "grant_type=authorization_code&client_id=" + clientId
					+ "&redirect_uri=" + redirectUri + "&code=" + code + "&client_secret=" + clientSecret;

			if(logger.isDebugEnabled())	{
				   logger.debug("---------------------->urlParameters :"+urlParameters);
			}

			String resp = executePost(accessTokenURL, urlParameters);
			if(logger.isDebugEnabled())	{
				   logger.debug("---------------------->resp :"+resp);
				}
			String accessToken = "";
			String sinaUid = "";
				ObjectMapper mapper = new ObjectMapper();
				try {
					accessToken = (String) mapper.readValue(resp, HashMap.class).get("access_token");
					sinaUid = (String) mapper.readValue(resp, HashMap.class).get("uid");
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			// Step 3 - Create connection
			if(logger.isDebugEnabled())	{
			   logger.debug("---------------------->accessToken :"+accessToken);
			   logger.debug("---------------------->user :"+LoginHelper.getUserId());
			}
			//保存accessToken到redis
			mongoUserDao.saveAccessToken(LoginHelper.getUserId(),
					new SinaAccessToken(accessToken, sinaUid));

//			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
//			response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);
		}

        if(callbackUrl!=null){
        	return "redirect:"+callbackUrl;
        }else{
		    return "bindsuccess";
        }
	}

	// POST request helper
	private String executePost(String targetURL, String urlParameters) {
		URL url = null;
		HttpsURLConnection connection = null;
		try {

			//创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager ()};
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			//从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			//创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			url = new URL(targetURL);
			connection = (HttpsURLConnection)url.openConnection();
			connection.setSSLSocketFactory(ssf);



			// Create connection
			//url = new URL(targetURL);
			//connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
                        "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}

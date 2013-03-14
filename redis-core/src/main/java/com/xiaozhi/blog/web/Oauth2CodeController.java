package com.xiaozhi.blog.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;




//@Controller
public class Oauth2CodeController {


	private static Log logger = LogFactory.getLog(Oauth2CodeController.class);

	private static final String clientId = "tonr";
	private static final String clientSecret = "secret";

	private static final String oauthBase = "http://localhost:8080/test/oauth";
	private static final String authorizeURL = oauthBase + "/authorize";
	private static final String accessTokenURL = oauthBase + "/token";
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
	@RequestMapping(value = "/bind/{user}", method = RequestMethod.GET)
	public String bind(@PathVariable String user,HttpServletRequest request, Model model) throws IOException {

		String code = request.getParameter("code");
		String redirectUri = request.getRequestURL().toString();
		if(logger.isDebugEnabled())	{
			   logger.debug("---------------------->redirectUri :"+redirectUri);
		}
		if (code == null || "".equals(code)) {

			// Step 1 - Redirect user to provider for authorization
			String url = authorizeURL + "?response_type=code&scope=read&client_id="+ clientId + "&redirect_uri=" + redirectUri;
			//response.sendRedirect(url);
			return "redirect:"+url;

		} else {

			// Step 2 - Exchange for access grant
			String urlParameters = "grant_type=authorization_code&client_id=" + clientId
					+ "&client_secret=" + clientSecret
					+ "&redirect_uri=" + redirectUri + "&code=" + code;

			String resp = executePost(accessTokenURL, urlParameters);
			if(logger.isDebugEnabled())	{
				   logger.debug("---------------------->resp :"+resp);
				}
			String accessToken = "";

				ObjectMapper mapper = new ObjectMapper();
				try {
					accessToken = (String) mapper.readValue(resp, HashMap.class).get("access_token");
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
			}
			//保存accessToken到redis
			//retwis.saveAccessToken(user, accessToken);

//			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
//			response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);
		}


		return "bindsuccess";
	}

	// POST request helper
	private String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
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

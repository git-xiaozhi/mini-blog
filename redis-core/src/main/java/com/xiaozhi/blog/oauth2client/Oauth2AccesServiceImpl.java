package com.xiaozhi.blog.oauth2client;

import java.net.URI;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author Ryan Heaton
 */
//@Service
public class Oauth2AccesServiceImpl implements Oauth2AccesService {

	private static Log logger = LogFactory.getLog(Oauth2AccesServiceImpl.class);

	@Value(value = "#{globalProperties['trustedMessageURL']}")
	private String trustedMessageURL;

	@Value(value = "#{globalProperties['xmlPhotoListURL']}")
	private String xmlPhotoListURL;

	@Value(value = "#{globalProperties['jsonPhotoListURL']}")
	private String jsonPhotoListURL;

	@Autowired
	private OAuth2RestTemplate oAuth2RestTemplate;

	@Autowired
	private RestTemplate restTemplate;


	/* (non-Javadoc)
	 * @see com.xiaozhi.blog.oauth2client.Oauth2AccesService#getTrustedMessage()
	 */
	@Override
	public String getTrustedMessage() {
		return this.oAuth2RestTemplate.getForObject(URI.create(trustedMessageURL), String.class);
		//return this.restTemplate.getForObject(URI.create(trustedMessageURL), String.class);
	}

	@Override
	public String getPhotoListXML() {
		ResponseEntity<String> responseEntity = oAuth2RestTemplate.getForEntity(URI.create(xmlPhotoListURL),String.class);
		//ResponseEntity<String> responseEntity = restTemplate.getForEntity(URI.create(xmlPhotoListURL),String.class);
		return responseEntity.getBody();
	}

	@Override
	public String getPhotoListJson() {
		OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		logger.debug("--------------------------->accessToken :"+accessToken.getValue());
		logger.debug("--------------------------->refreshToken :"+accessToken.getRefreshToken().getValue());
		logger.debug("--------------------------->jsonPhotoListURL :"+jsonPhotoListURL);

		ResponseEntity<String> responseEntity = oAuth2RestTemplate.getForEntity(URI.create(jsonPhotoListURL),String.class);
		//ResponseEntity<String> responseEntity = restTemplate.getForEntity(URI.create(jsonPhotoListURL),String.class);
		return responseEntity.getBody();
	}

}

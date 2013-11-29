package com.clo.auth2;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for retrieving the model for and displaying the confirmation page for access to a protected resource.
 *
 * @author Ryan Heaton
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AccessConfirmationController {

	private ClientDetailsService clientDetailsService;

	/**
	 * 用户授权默认redirect的url,这里自定义他的认证页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model) throws Exception {
		AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");
		ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		model.put("auth_request", clientAuth);
		model.put("client", client);
		return new ModelAndView("access_confirmation", model);
	}

	/**
	 * 自定义认证出错页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/oauth/error")
	public String handleError(Map<String,Object> model) throws Exception {
		// We can add more stuff to the model here for JSP rendering.  If the client was a machine then
		// the JSON will already have been rendered.
		model.put("message", "There was a problem with the OAuth2 protocol");
		return "oauth_error";
	}

	@Autowired
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
}

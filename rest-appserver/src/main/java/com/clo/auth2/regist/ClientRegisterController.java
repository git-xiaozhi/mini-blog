package com.clo.auth2.regist;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tianji.test.core.redis.LoginHelper;



@Controller
public class ClientRegisterController {

    @Autowired
	private ClientRegisterServer clientRegisterServer;



	@RequestMapping("/oauth/register")
	public String clientRegister(Model model) throws Exception {
		BaseClientDetails clientDetails = new BaseClientDetails();
		clientDetails.setAuthorities(LoginHelper.getUserDetails().getAuthorities());
     model.addAttribute("clientDetails", this.clientRegisterServer.addClient(clientDetails));
     return "registsuccess";
	}


	@RequestMapping("/oauth/delete")
	public String clientDel(Model model) throws Exception {
     this.clientRegisterServer.delClient("tonr");
     return "registsuccess";
	}
}
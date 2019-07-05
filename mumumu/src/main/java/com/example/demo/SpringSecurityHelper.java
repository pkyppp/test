package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SpringSecurityHelper {
	private final UserService userService;
	private final SessionRegistry sessionRegistry;
	
	public static LoginUser getLoginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() != null && LoginUser.class.isAssignableFrom(auth.getPrincipal().getClass() )) {
			return (LoginUser) auth.getPrincipal();
		}
		return null;
	}
	
	public static String getSmypgid() {
		LoginUser loginUser = SpringSecurityHelper.getLoginUser();
		if (CheckUtil.isEmpty(loginUser)) {
			return null;
		}
		return loginUser.getUsername();
	}
	
	public static boolean getAuthenticated() {
		LoginUser loginUser = SpringSecurityHelper.getLoginUser();
		if (CheckUtil.isEmpty(loginUser)) {
			return false;
		}
		return true;
	}
	
	public void removeSession(HttpServletRequest request) {
		if (sessionRegistry != null) {
			LoginUser loginUser = getLoginUser();
			List<SessionInformation> sessionInfoList = 
					sessionRegistry.getAllSession(loginUser, false);
			if (CollectionUtils.isNotEmpty(sessionInfoList)) {
				sessionInfoList.forEach(si -> {
					si.expireNow();
					sessionRegistry.removeSessionInformation(sessionInfo.getSessionId());
				});
			}
		}
		if (request != null) {
			new SecurityContextLogoutHandler().logout(request, null, null);
		}
	}
}

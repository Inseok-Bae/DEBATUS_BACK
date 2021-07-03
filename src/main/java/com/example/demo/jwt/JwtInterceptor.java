package com.example.demo.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if(request.getMethod().equals("OPTIONS")) { return true; } 
		else {
			String token = request.getHeader("jwt-auth-token");

			if (token != null && token.length() > 0) {
				String newToken = jwtService.checkValid(token);
				
				response.setHeader("jwt-auth-token", newToken);
				
				return true;
			} else {
				throw new RuntimeException("no token");
			}
		}
		
	}

}

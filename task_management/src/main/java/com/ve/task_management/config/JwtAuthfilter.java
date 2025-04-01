package com.ve.task_management.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//this auth filter is to get the data from database using the jwt token.
@Component
public class JwtAuthfilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtserviceImplementation;
	
	@Autowired
	ApplicationContext applicationContext;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String authheader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		if(authheader != null && authheader.startsWith("Bearer "))
		{
			token = authheader.substring(7);
			username = jwtserviceImplementation.extractUsername(token);
		}
		
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userDetails = applicationContext.getBean(CustomUserdetailsservice.class).loadUserByUsername(username);
			if(jwtserviceImplementation.validatetoken(token,userDetails))
			{
				//bringing the username and user details objects from the database.
				UsernamePasswordAuthenticationToken authenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				//authentication token also need to know about the request object.
				authenticationToken.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				//by doing this below we are adding the authentication token in the chain
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		//telling the filter to go for another filter after this is done.
		filterChain.doFilter(request, response);
	}

}

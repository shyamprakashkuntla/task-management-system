package com.ve.task_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity
public class Securityconfiguration {
	
	private final UserDetailsService userDetailsService;
	private final JwtAuthfilter authfilter;
	private HandlerExceptionResolver exceptionResolver;
//	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	public Securityconfiguration(UserDetailsService userDetailsService, JwtAuthfilter authfilter,
	                             @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.authfilter = authfilter;
		this.exceptionResolver = exceptionResolver;
	}
	

	/*
//	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		//this is with method reference	
	return	httpSecurity
		//.csrf(customizer->customizer.disable())
		.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(request->request
				.requestMatchers("/api/auth/register","/api/auth/login","/forgotPassword/**").permitAll()
				.anyRequest().authenticated())
		.httpBasic(Customizer.withDefaults())
		.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(authfilter, UsernamePasswordAuthenticationFilter.class)
		.build();
	}
	//in memory user details are through console, through application.properties file
	// using the below ones.
	*/
	
	

	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    return httpSecurity
	        .csrf(AbstractHttpConfigurer::disable)
	        .authorizeHttpRequests(request -> request
	        		.requestMatchers(
	                        "/swagger-ui/**",         // Swagger UI
	                        "/v3/api-docs/**",        // OpenAPI docs
	                        "/swagger-resources/**",  // Swagger resources
	                        "/webjars/**"             // Swagger UI assets
	                    ).permitAll()
	            .requestMatchers("/api/auth/register", "/api/auth/login", "/forgotPassword/**").permitAll()
	            .requestMatchers("/api/auth/getAllUsers").hasRole("ADMIN")  // Only Admin can access all users
	            .requestMatchers("/api/auth/User/**").hasAnyRole("ADMIN", "USER")  // Both Admin and User can access their own data
	            .requestMatchers("/api/auth/deleteUser/**").hasAnyRole("ADMIN", "USER")
	            .requestMatchers("/api/auth/updateUser/**").hasAnyRole("ADMIN", "USER")
	            .anyRequest().authenticated()
	        )
	        .httpBasic(Customizer.withDefaults())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	        .exceptionHandling(exception->exception.authenticationEntryPoint(authenticationEntryPoint))
	        .exceptionHandling(exception -> exception
	                // Delegate to @ControllerAdvice for 401
	                .authenticationEntryPoint((request, response, authException) -> 
	                    exceptionResolver.resolveException(
	                        request, response, null, authException
	                    )
	                )
	                // Delegate to @ControllerAdvice for 403
	                .accessDeniedHandler((request, response, accessDeniedException) -> 
	                    exceptionResolver.resolveException(
	                        request, response, null, accessDeniedException
	                    )
	                )
	            )
	        .addFilterBefore(authfilter, UsernamePasswordAuthenticationFilter.class)
	        .build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	//for jwt authentication this authentication manager needed to be handled
	//behind the scenes the authenticaiton provider uses authentication manager.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{
		return authenticationConfiguration.getAuthenticationManager();
	}
}

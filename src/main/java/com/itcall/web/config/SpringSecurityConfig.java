/**
 * 
 */
package com.itcall.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.itcall.web.config.filter.LoggerRequestFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>개정이력(Modification Information)
 * 
 *     수정일           수정자     수정내용
 * ------------------------------------------
 * 2024. 7. 31.    KUEE-HAENG LEE :   최초작성
 * </pre>
 * @author KUEE-HAENG LEE
 * @version 1.0.0
 * @see
 * @since 2024. 7. 31.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	private static final String DEF_PERMIT_MATCHER_URI_PATTERNS = "/static/**,/login,/logout,/img/**,/image/**,/about/**,/css/**,/lib*/**,/js/**,/media/**,/public/**,/sso/**,/test/**";
	private static final String DEF_PERMIT_MATCHER_URI_FOR_LOGGER_FILTER = "/**";

	@Value("${spring.security.permit-all.uris:"+DEF_PERMIT_MATCHER_URI_PATTERNS+"}")
	private String permitAllUriPatters;
	@Value("${spring.security.filter.logger.uris:"+DEF_PERMIT_MATCHER_URI_FOR_LOGGER_FILTER+"}")
	private String onFilterForLoggerUriPatters;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		LoggerRequestFilter loggerRequestFilter = new LoggerRequestFilter();
//		FilterRegistrationBean<OncePerRequestFilter> regBean = new FilterRegistrationBean<>();
//		regBean.setFilter(loggerRequestFilter);
//		regBean.addUrlPatterns(onFilterForLoggerUriPatters.split(","));
//		regBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1); // 최상위 필터:
//		// regBean.setOrder(SecurityProperties.BASIC_AUTH_ORDER - 1); // 마지막임 필터:
		log.debug("Filtering to LoggerRequestFilter Url-Patterns: {}", Arrays.asList(onFilterForLoggerUriPatters.split(",")));
		
		http
				// .addFilterAfter(loggerRequestFilter, CorsFilter.class)
				.addFilterBefore(loggerRequestFilter, UsernamePasswordAuthenticationFilter.class)
				
				.authorizeHttpRequests( authorize -> authorize
						.requestMatchers(permitAllUriPatters.split(",")).permitAll()
						.anyRequest().authenticated()
				)
		
		.httpBasic(it -> {})
		.formLogin(it -> {})
		;
		
		return http.build();
	}

}

/**
 * 
 */
package com.itcall.web.config.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.itcall.web.config.filter.wrapper.CustomRequestWrapper;
import com.itcall.web.config.filter.wrapper.CustomResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개정이력(Modification Information)
 * Input에 대한 로깅을 남기거나 Request-Body에 대한 Access를 위한 필터.
 *     수정일           수정자     수정내용
 * ------------------------------------------
 * 2024. 7. 24.    KUEE-HAENG LEE :   최초작성
 * </pre>
 * 
 * @author KUEE-HAENG LEE
 * @version 1.0.0
 * @see
 * @since 2024. 7. 24.
 */
@Slf4j
// @Component
public class LoggerRequestFilter extends OncePerRequestFilter {

	private static final RequestMatcher MATCHER_BOTH = RequestMatchers.anyOf(Arrays.asList("/test/both*,/test/all/**".split(",")).stream().map(m -> new AntPathRequestMatcher(m)).toArray(AntPathRequestMatcher[]::new));
	private static final RequestMatcher MATCHER_REQUEST = RequestMatchers.anyOf(Arrays.asList("/test/req*,/test/ask/**".split(",")).stream().map(m -> new AntPathRequestMatcher(m)).toArray(AntPathRequestMatcher[]::new));
	private static final RequestMatcher MATCHER_RESPONSE = RequestMatchers.anyOf(Arrays.asList("/test/res*,/test/answer/**".split(",")).stream().map(m -> new AntPathRequestMatcher(m)).toArray(AntPathRequestMatcher[]::new));
	private static final String FILTERED_MEDIA_MAIN_TYPE = "text,application";// Arrays.asList(new String[] {"text/html","application/xml"}).stream().map(m -> new MediaType(m)).toList();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		/*** Body가 없거나 + Chunked 이거나 + Integer.MAX_VALUE(2,147,483,647) 보다 크면 처리하지 않는다.
		 * AND: Request의 ContentType의 Main 타입 중 Filter처리 가능 타입만 대상으로 한다. ***/
		if(request.getContentLength() > 0
				&& Objects.nonNull(request.getContentType())
				&& FILTERED_MEDIA_MAIN_TYPE.contains(MediaType.valueOf(request.getContentType()).getType())
				
//				mediaType.getType().equals("text")
//				|| mediaType.getType().equals("application")
				
//				new MediaType("text/*").isCompatibleWith(mediaType)
//				|| new MediaType("application/*").isCompatibleWith(mediaType)
				
//				FILTERED_MEDIA_TYPE.stream().filter(m -> m.isCompatibleWith(mediaType)).findAny().isPresent()
				
				) {
			
			log.info("Requested MediaTypes: {}", request.getContentType()); // This is MainType/SubType/... !!!
			
			if(MATCHER_BOTH.matches(request)) {
			// if(requestUri.matches("--- Request uri Patterns regular expression 요청/응답 모두 먼저읽고 변경하기 ---")) {
				
				filterForBoth(request, response, filterChain);
				
			} else if(MATCHER_REQUEST.matches(request)) {
			// } else if(requestUri.matches("--- Request uri Patterns regular expression 요청만 모두 먼저읽고 변경하기 ---")) {
				
				filterForRequested(request, response, filterChain);
				
			} else if(MATCHER_RESPONSE.matches(request)) {
			// } else if(requestUri.matches("--- Request uri Patterns regular expression 응답만 모두 먼저읽고 변경하기 ---")) {
				
				filterForResponded(request, response, filterChain);
				
			} else {
				// 이외 필터처리 안하는 경우.
				filterChain.doFilter(request, response);
			}
		} else {
			// 이외 필터처리 안하는 경우.
			filterChain.doFilter(request, response);
		}
	}

	private void filterForBoth(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// 요청정보 복제
		CustomRequestWrapper requestWrapper = new CustomRequestWrapper(request);
		// 응답정보 복제
		CustomResponseWrapper responseWrapper = new CustomResponseWrapper(request, response);
		try {
			/*********************************
			 * 요청정보 BODY를 사전에 읽어 볼 수 있고, 수정 또한 가능하다.
			 *********************************/
			String requestBodyOrigin = requestWrapper.getBodyOrigin(); // 요청정보 BODY를 변경해도 원래 BODY는 항상 읽어들일 수 있다.
			requestWrapper.setBody(" --- 요청정보 BODY를 서비스쪽에서 다르게 읽을수 있게 통째로 변경할 수 있다 --- \n --- 아래는 원래 전송하려는 Request BODY 임. --- \n" + requestBodyOrigin);
			String requestBody = requestWrapper.getBody(); // 요청정보 BODY를 읽어들인다. 검사를 위해....
			log.info("Requested Body: {}\nFinal Requested Body: {}", requestBodyOrigin, requestBody);
			// 실제 업무서비스로 전달.
			filterChain.doFilter(requestWrapper, responseWrapper);
			
			/*********************************
			 * 응답정보 BODY를 사전에 읽어 볼 수 있고, 수정 또한 가능하다.
			 *********************************/
			String responseBodyOrigin = responseWrapper.getResponseRawBodyOriginData(); // 응답정보 BODY를 변경해도 원래 BODY는 항상 읽어들일 수 있다.
			responseWrapper.setResponseBody(" --- 응답정보 BODY를 호출단에 다르게 읽을수 있게 통째로 변경할 수 있다 --- \n --- 아래는 원래 전송하려는 Response BODY 임. --- \n" + responseBodyOrigin);
			String responseBody = responseWrapper.getResponseBody(); // 응답정보 BODY를 읽어들인다. 검사를 위해서...
			log.debug("Response Body: {}\nFinal Response Body: {}", responseBodyOrigin, responseBody);
		} finally {
			responseWrapper.sendData(); // 마지막에 꼭 실행해줘야 응답이 나간다. 실제 여기서 호출지로 최종전송하며, 더이상 읽을 수 없다. close는 시스템에서 하게 한다.
		}
	}

	private void filterForRequested(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// 요청정보 복제
		CustomRequestWrapper requestWrapper = new CustomRequestWrapper(request);
		
		/*********************************
		 * 요청정보 BODY를 사전에 읽어 볼 수 있고, 수정 또한 가능하다.
		 *********************************/
		String requestBodyOrigin = requestWrapper.getBodyOrigin(); // 요청정보 BODY를 변경해도 원래 BODY는 항상 읽어들일 수 있다.
		requestWrapper.setBody(" --- 요청정보 BODY를 서비스쪽에서 다르게 읽을수 있게 통째로 변경할 수 있다 --- \n --- 아래는 원래 전송하려는 Request BODY 임. --- \n" + requestBodyOrigin);
		String requestBody = requestWrapper.getBody(); // 요청정보 BODY를 읽어들인다. 검사를 위해....
		log.info("Requested Body: {}\nFinal Requested Body: {}", requestBodyOrigin, requestBody);
		
		// 실제 업무서비스로 전달.
		filterChain.doFilter(requestWrapper, response);
	}

	private void filterForResponded(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// 응답정보 복제
		CustomResponseWrapper responseWrapper = new CustomResponseWrapper(request, response);
		try {
			// 실제 업무서비스로 전달.
			filterChain.doFilter(request, responseWrapper);
			
			/*********************************
			 * 응답정보 BODY를 사전에 읽어 볼 수 있고, 수정 또한 가능하다.
			 *********************************/
			String responseBodyOrigin = responseWrapper.getResponseRawBodyOriginData(); // 응답정보 BODY를 변경해도 원래 BODY는 항상 읽어들일 수 있다.
			responseWrapper.setResponseBody(" --- 응답정보 BODY를 호출단에 다르게 읽을수 있게 통째로 변경할 수 있다 --- \n --- 아래는 원래 전송하려는 Response BODY 임. --- \n" + responseBodyOrigin);
			String responseBody = responseWrapper.getResponseBody(); // 응답정보 BODY를 읽어들인다. 검사를 위해서...
			log.debug("Response Body: {}\nFinal Response Body: {}", responseBodyOrigin, responseBody);
		} finally {
			responseWrapper.sendData(); // 마지막에 꼭 실행해줘야 응답이 나간다. 실제 여기서 호출지로 최종전송하며, 더이상 읽을 수 없다. close는 시스템에서 하게 한다.
		}
	}

}

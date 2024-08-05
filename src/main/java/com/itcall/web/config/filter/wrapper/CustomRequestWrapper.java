/**
 * 
 */
package com.itcall.web.config.filter.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.springframework.util.StringUtils;

import com.itcall.web.config.filter.wrapper.copier.ServletInputStreamCopier;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개정이력(Modification Information)
 * 요청 Body를 Copy하여 저장 후 제공한다.
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
public class CustomRequestWrapper extends HttpServletRequestWrapper {

	@Getter
	private byte[] requestOriginRawBodyData;
	@Getter@Setter
	private byte[] requestRawBodyData;
	@Getter
	private Charset charset;

	public CustomRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		String charset = request.getCharacterEncoding();
		if(StringUtils.hasText(charset)) {
			this.charset = Charset.forName(charset);
		} else {
			this.charset = Charset.forName("UTF-8");
		}
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// byteArrayOutputStream.write(IOUtils.toByteArray(request.getInputStream()));
			/****
			while(request.getInputStream().available()>0)
				byteArrayOutputStream.write(request.getInputStream().read());
			****/
			request.getInputStream().transferTo(byteArrayOutputStream);
			byteArrayOutputStream.flush();
			this.requestOriginRawBodyData = this.requestRawBodyData = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		} catch (IOException e) {
			log.error("Cannot read from request.inputStream. It's bed connection or worng data.", e);
			throw e;
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStreamCopier(this.requestRawBodyData);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), this.charset));
	}

	@Override
	public ServletRequest getRequest() {
		/*** 이건 둘중에 어떤것을 반환하는게 맞는지 체크해봐야한다. ***/
		// return super.getRequest();
		return this;
	}

	/** 요청 전문 - Request-Body(수정된 Body - 서비스에 제공될...)를 문자열로 가져온다. **/
	public String getBody() {
		return new String(this.requestRawBodyData, this.charset);
	}
	/** 요청 전문 원본 - Request-Body(최초 수신한 원본)를 문자열로 가져온다. **/
	public String getBodyOrigin() {
		return new String(this.requestOriginRawBodyData, this.charset);
	}
	/** 요청 전문 - Request-Body를 교체한다. - 원본은 교체 불가하며, 서비스에 제공할 Body만 교체 가능하다. **/
	public void setBody(String body) {
		this.requestRawBodyData = body.getBytes(this.charset);
	}
}

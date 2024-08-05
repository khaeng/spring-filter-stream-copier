/**
 * 
 */
package com.itcall.web.config.filter.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import org.springframework.util.StringUtils;

import com.itcall.web.config.filter.wrapper.copier.ServletOutputStreamCopier;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개정이력(Modification Information)
 * 
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
public class CustomResponseWrapper extends HttpServletResponseWrapper {

	private final Charset requestedCharset;
	private final ByteArrayOutputStream byteArrayOutputStream;
	// private Charset responseCharset;
	private byte[] responseRawBodyOriginData;

	public CustomResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
		super(response);
		String charset = request.getCharacterEncoding();
		if(StringUtils.hasText(charset)) {
			this.requestedCharset = Charset.forName(charset);
		} else {
			this.requestedCharset = Charset.forName("UTF-8");
		}
		this.byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStreamCopier(this.byteArrayOutputStream);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
//		if(StringUtils.hasText(getCharacterEncoding())) {
//			this.responseCharset = Charset.forName(getCharacterEncoding());
//		}
		Writer targetWriter = new OutputStreamWriter(getOutputStream(), this.requestedCharset);
		return new PrintWriter(targetWriter);
	}

	@Override
	public ServletResponse getResponse() {
		/*** 이건 둘중에 어떤것을 반환하는게 맞는지 체크해봐야한다. ***/
		// return super.getResponse();
		return this;
	}

	/**
	 * <b>요청 전문을 사전에 체크 및 검사하기위해서 읽어간다.</b><br/>
	 * @return
	 * @return String
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @throws IOException 
	 * @since 2020. 11. 11. 오후 12:34:32
	 */
	public String getResponseBody() throws IOException {
		this.byteArrayOutputStream.flush(); // 서비스단에서 응답처리가 완료됐을떼 호출되는 것이므로 flush를 한번 더 해준다.
		return new String(this.byteArrayOutputStream.toByteArray(), this.requestedCharset);
	}

	/**
	 * <b>응답 전문을 보내기전에 암호화하는 등의 요건에 의해 다시 써야할 경우 모든 BODY를 새로운 데이터로 교체할수있다. 암복호화의 경우에 쓰인다.</b><br/>
	 * @param body
	 * @return void
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @since 2020. 11. 11. 오후 12:36:16
	 */
	public void setResponseBody(String body) throws UnsupportedEncodingException, IOException {
		this.byteArrayOutputStream.flush();
		storeOriginResBody(); // 현재까지 Write된 Response-Data를 Origin에 저장하고 새로운 body를 다시 담는다.
		this.byteArrayOutputStream.reset(); // 초기화 해버리고...
//		if(StringUtils.hasText(getCharacterEncoding())) {
//			this.responseCharset = Charset.forName(getCharacterEncoding());
//		}
		this.byteArrayOutputStream.write(body.getBytes(this.requestedCharset));
		this.byteArrayOutputStream.flush();
	}

	/**
	 * <b>로깅 시 응답전문의 암호화 전 데이터를 보관해 놓는다.</b><br/>
	 * @return void
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @since 2020. 11. 13. 오후 3:29:51
	 */
	public void storeOriginResBody() {
		// this.byteArrayOutputStream.flush(); // 해야하나?
		this.responseRawBodyOriginData = this.byteArrayOutputStream.toByteArray();
	}

	/**
	 * <b>암복호화 등으로 가공되기전의 최초 응답전문의 Body를 가져온다.</b><br/>
	 * @return
	 * @return String
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @since 2020. 11. 16. 오후 2:38:27
	 */
	public String getResponseRawBodyOriginData() {
		// this.byteArrayOutputStream.flush(); // 해야하나?
		if(this.responseRawBodyOriginData!=null) {
			return new String(this.responseRawBodyOriginData, this.requestedCharset);
		} else if(this.byteArrayOutputStream.toByteArray()!=null) {
			return new String(this.byteArrayOutputStream.toByteArray(), this.requestedCharset);
		} else {
			return "";
		}
	}


	/**
	 * <b>실제 Client에 전송하는 역할을 한다. 서비스에서 전송한 데이터를 그대로 보낸다.</b><br/>
	 * @throws IOException
	 * @return void
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @since 2020. 11. 11. 오후 1:39:34
	 */
	public void sendData() throws IOException {
		log.debug("서비스단에서 전송하는 데이터를 그대로 요청지에 전달한다. 전송데이터크기[{}]", this.byteArrayOutputStream.size());
		this.byteArrayOutputStream.flush(); // 서비스단에서 응답처리가 완료됐을떼 호출되는 것이므로 flush를 한번 더 해준다.
		sendData(this.byteArrayOutputStream.toByteArray());
		this.byteArrayOutputStream.close(); // 더이상 사용할 수 없게 한다.
	}
	/**
     * <b>실제 Client에 전송하는 역할을 한다. 서비스에서 전송한 데이터를 가공하여 filter에서 전달한 데이터를 보낸다.</b><br/>
	 * @param responseRawBodyData
	 * @throws IOException
	 * @return void
	 * @author Lee Kuee-Haeng (khaeng@nate.com)
	 * @since 2020. 11. 11. 오후 1:40:14
	 */
	private void sendData(byte[] responseRawBodyData) throws IOException {
		super.getOutputStream().write(responseRawBodyData);
		super.getOutputStream().flush();
	}

	@Override
	public void flushBuffer() throws IOException {
		super.getOutputStream().write(this.byteArrayOutputStream.toByteArray());
		this.byteArrayOutputStream.reset(); // 초기화
		super.getOutputStream().flush();
	}

}

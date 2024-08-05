/**
 * 
 */
package com.itcall.web.config.filter.wrapper.copier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개정이력(Modification Information)
 * 
 * Response를 응답하기전에 Response 데이터를 복제하여 검증 후 복제된 Response를 제공하여 2회 이상 Response 데이터에 접근 할 수 있게 한다.
 * 대부분 로그성 작업이나, 보안검증등을 수행할 때 사용한다. 비즈니스적으로 사용하는 케이스는 설계적으로 다시 해석해 보아야 한다.
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
public class ServletOutputStreamCopier extends ServletOutputStream {

	private final ByteArrayOutputStream byteArrayOutputStream;
	private boolean isOpen;

	public ServletOutputStreamCopier(final ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
		this.isOpen = true;
	}

	@Override
	public boolean isReady() {
		return isOpen;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		log.warn("Not supported WriteListener!!!");
		throw new RuntimeException("Not implemented"); // 제공하지 않음.
	}

	@Override
	public void write(int b) throws IOException {
		this.byteArrayOutputStream.write(b);
	}

//	@Override
//	public void flush() throws IOException {
//		super.flush();
//	}

	@Override
	public void close() throws IOException {
		this.isOpen = false;
		this.byteArrayOutputStream.close();
		super.close();
	}

}

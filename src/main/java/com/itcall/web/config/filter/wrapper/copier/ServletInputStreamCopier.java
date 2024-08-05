/**
 * 
 */
package com.itcall.web.config.filter.wrapper.copier;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개정이력(Modification Information)
 * 
 * Request를 복제하여 Request와 동일하게 읽어갈 수 있게 제공.
 * 대규모 Body 데이터는 수행를 회피해야하고, API등에서 Logging처리를 사전에 하거나, Body를 꼭 먼저 읽어야 할 경우 사용한다.
 * 비즈니스 적으로 Body를 먼저 읽어야 할 경우는 대부분 비즈니스를 잘못 설계한 케이스가 대부분이라는 거 명심하자.
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
public class ServletInputStreamCopier extends ServletInputStream {

	private final ByteArrayInputStream byteArrayInputStream;
	private boolean isOpen;

	public ServletInputStreamCopier(byte[] requestRawBodyData) {
		this.byteArrayInputStream = new ByteArrayInputStream(requestRawBodyData);
		this.isOpen = true;
	}

	@Override
	public boolean isFinished() {
		return !this.isOpen || this.byteArrayInputStream.available() == 0; // 닫혀있거나, 모두 읽은경우...
	}

	@Override
	public boolean isReady() {
		return isOpen;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		log.warn("Not supported ReadListener!!!");
		throw new RuntimeException("Not implemented"); // 제공하지 않음.
	}

	@Override
	public int read() throws IOException {
		return this.byteArrayInputStream.read(); // 읽어가는 곳. 모든 read계열은 최종적으로 read()를 통해서 읽어감.
	}

	@Override
	public void close() throws IOException {
		this.isOpen = false;
		this.byteArrayInputStream.close();
		super.close();
	}
}

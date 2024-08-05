/**
 * 
 */
package com.itcall.web.config.contents;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RestController
@RequiredArgsConstructor
@RequestMapping({"/test"})
public class LoggerFilterStreamCopierController {

	
	

    @GetMapping({"/both{key1}","/all/{key2}", "/req{key1}","/ask/{key2}", "/res{key1}","/answer/{key2}", "/nofilter/{key2}"})
    public Map<String, Object> testBoth(HttpServletRequest request
        , @PathVariable(name = "key1", required = false) String key1
        , @PathVariable(name = "key2", required = false) String key2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        request.getInputStream().transferTo(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] inputBodyData = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        String requestBody = new String(inputBodyData, request.getCharacterEncoding());
        log.info("수신 바디: {}", requestBody);
        return ImmutableMap.of(
                "requested-uri", request.getRequestURI()
                , "requested-body", Objects.isNull(requestBody) ? "null" : requestBody
                , "requested-key1", Objects.isNull(key1) ? "null" : key1
                , "requested-key2", Objects.isNull(key2) ? "null" : key2);
    }




}

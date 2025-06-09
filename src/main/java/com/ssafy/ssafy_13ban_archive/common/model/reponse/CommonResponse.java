package com.ssafy.ssafy_13ban_archive.common.model.reponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * 공통 응답 형식 클래스
 * CommonResponse로 모든 restController 반환 통일
 * Header 추가가 필요할 시 builder 패턴 활용
 * @param <T> 응답에 넣을 body 내용
 */

public class CommonResponse<T> extends ResponseEntity<ResponseWrapper<T>> {

    /**
     * 기본 생성자 (header 없음)
     * @param body Request에 넣을 body
     * @param status HttpStatus 값
     */
    public CommonResponse(T body, HttpStatusCode status) {
        super(new ResponseWrapper<T>(status.value(), body), status);
    }

    /**
     * 기본 생성자 (header 있음), 일반적으로 builder에서 쓰는 생성자
     * @param body Request에 넣을 body
     * @param status HttpStatus 값
     * @param headers 추가할 HttpHeaders
     */
    public CommonResponse(T body, HttpStatusCode status, HttpHeaders headers) {
        super(new ResponseWrapper<T>(status.value(), body), headers, status);
    }


    /**
     * CommonResponseBuilder를 호출하기 위한 static method <p/>
     * body에 넣을 타입이 있으면, <T>builder() 이런식으로 호출하기
     * @return CommonResponseBuilder
     * @param <T> CommonResponse body에 타입 (Wrapper로 자동으로 wrap됨)
     */
    public static <T> CommonResponseBuilder<T> builder() {
        return new CommonResponseBuilder<>();
    }

    public static class CommonResponseBuilder<T> {
        private HttpStatusCode status = HttpStatus.OK;
        private HttpHeaders headers = new HttpHeaders();
        private T body = null;

        public CommonResponseBuilder<T> status(HttpStatusCode status) {
            this.status = status;
            return this;
        }

        public CommonResponseBuilder<T> header(String key, String value) {
            this.headers.add(key, value);
            return this;
        }

        public CommonResponseBuilder<T> body(T body) {
            this.body = body;
            return this;
        }

        public CommonResponse<T> build() {
            return new CommonResponse<>(body, status, headers);
        }
    }
}

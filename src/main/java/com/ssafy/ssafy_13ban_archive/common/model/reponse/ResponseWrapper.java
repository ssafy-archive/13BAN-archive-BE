package com.ssafy.ssafy_13ban_archive.common.model.reponse;

import lombok.Getter;
import lombok.Setter;


/**
 * 공통형식 응답 클래스
 * @param <T> 추가할 세부 body
 */
@Getter
@Setter
public class ResponseWrapper<T> {
    int code;
    T body;

    ResponseWrapper(int code, T body) {
        this.code = code;
        this.body = body;
    }
}

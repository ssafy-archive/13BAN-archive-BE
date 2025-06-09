package com.ssafy.ssafy_13ban_archive.common.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode를 반환하기 위한 Body DTO
 */
@AllArgsConstructor
@Getter
public class ErrorBody {
    String code;
    String message;
}

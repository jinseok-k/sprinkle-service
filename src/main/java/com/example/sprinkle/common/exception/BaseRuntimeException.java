package com.example.sprinkle.common.exception;

import com.example.sprinkle.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
}

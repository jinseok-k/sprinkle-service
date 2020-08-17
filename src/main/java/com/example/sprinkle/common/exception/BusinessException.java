package com.example.sprinkle.common.exception;

import com.example.sprinkle.common.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessException extends BaseRuntimeException {

	public BusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}

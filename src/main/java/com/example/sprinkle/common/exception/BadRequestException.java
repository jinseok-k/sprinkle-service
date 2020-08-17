package com.example.sprinkle.common.exception;

import com.example.sprinkle.common.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseRuntimeException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}

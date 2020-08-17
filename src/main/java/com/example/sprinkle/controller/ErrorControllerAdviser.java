package com.example.sprinkle.controller;

import com.example.sprinkle.common.code.ErrorCode;
import com.example.sprinkle.common.exception.BaseRuntimeException;
import com.example.sprinkle.common.response.ErrorInfo;
import com.example.sprinkle.common.response.RestResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ErrorControllerAdviser {

	@ExceptionHandler(BaseRuntimeException.class)
	public ResponseEntity handleBaseRuntimeException(BaseRuntimeException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.error("errorCode = {}", errorCode);
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
		return ResponseEntity.status(responseStatus.code()).body(getErrorResult(errorCode));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity badRequest(Exception e) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		log.error("error = {}", e.getMessage());
		if(e instanceof HttpRequestMethodNotSupportedException) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		else if(e instanceof MissingServletRequestParameterException) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		else if(e instanceof MethodArgumentNotValidException) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		else {
			// 기타 에러 확인 용도
			StackTraceElement[] elements = e.getStackTrace();
			List<StackTraceElement> shortStack = IntStream
					.range(0, elements.length)
					.filter(i -> i < 10)
					.mapToObj(i -> elements[i])
					.collect(toList());
			log.error("# shortStack = {}", shortStack);
		}
		return ResponseEntity.status(httpStatus).body(getErrorResult(ErrorCode.KP0001));
	}

	private RestResult getErrorResult(ErrorCode errorCode) {
		ErrorInfo ErrorInfo = new ErrorInfo(errorCode.name(), errorCode.getErrorMessage());
		return new RestResult(ErrorInfo);
	}
}

package com.example.sprinkle.controller;

import com.example.sprinkle.common.response.RestResult;
import org.springframework.http.ResponseEntity;

public class BaseController {

	protected ResponseEntity success(Object data) {
		return ResponseEntity.ok(new RestResult(data));
	}
}

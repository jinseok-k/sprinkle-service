package com.example.sprinkle.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult {
	private boolean success = true;
	private Object data;
	private ErrorInfo error;

	public RestResult(Object data) {
		this.success = true;
		this.data = data;
	}

	public RestResult(ErrorInfo errorInfo) {
		this.success = false;
		this.error = errorInfo;
	}
}

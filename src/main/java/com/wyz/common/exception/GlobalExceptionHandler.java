package com.wyz.common.exception;

import com.wyz.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = ShiroException.class)
	public Result handler(ShiroException e) {
		log.error("运行时异常：-------------{}", e.getMessage());
		return Result.fail(401, e.getMessage(), null);
	}


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = RuntimeException.class)
	public Result handler(RuntimeException e) {
		log.error("运行时异常：-------------{}", e.getMessage());
		return Result.fail(e.getMessage());
	}

	//实体校验异常，如邮箱格式错误，用户名为空等等
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Result handler(MethodArgumentNotValidException e) {
		log.error("实体校验异常：-------------{}", e.getMessage());
		BindingResult result = e.getBindingResult();
		ObjectError objectError = result.getAllErrors().stream().findFirst().get();
		return Result.fail(objectError.getDefaultMessage());
	}

	//Assert异常
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public Result handler(IllegalArgumentException e) {
		log.error("Assert异常：-------------{}", e.getMessage());
		return Result.fail(e.getMessage());
	}


}

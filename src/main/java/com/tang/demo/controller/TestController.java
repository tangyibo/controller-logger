package com.tang.demo.controller;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tang.demo.annotation.Log;
import com.tang.demo.domain.ResponseResult;

@RestController
public class TestController {

	@Log(name = "测试模块", description = "'这是一个：'+#person.name")
	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseResult<Object> getModelNames(@RequestBody @Valid Person person, BindingResult bindResult) {
		if (bindResult.getErrorCount() > 0) {
			return ResponseResult.failed(-1, bindResult.getFieldError().getDefaultMessage());
		}

		return ResponseResult.success(person);
	}
}

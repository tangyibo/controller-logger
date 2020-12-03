package com.tang.demo.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person{
	@NotNull(message = "id不能为空")
	private Integer id;
	
	@NotBlank(message = "name不能为空")
	private String name;
	
	@NotNull(message = "salary不能为空")
	private Float salary;
	
	@NotNull(message = "intrersts不能为空")
	private List<String> intrersts;
}

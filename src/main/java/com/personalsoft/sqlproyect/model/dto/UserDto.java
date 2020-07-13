package com.personalsoft.sqlproyect.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder
@AllArgsConstructor
@Generated
public class UserDto {
	
	private Integer id;	
	@Size(min=10)
	private String nombre;
	private String email;
	@Min(18)
	private int age;

}

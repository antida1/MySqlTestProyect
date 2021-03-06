package com.personalsoft.sqlproyect.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.personalsoft.sqlproyect.exception.UserException;
import com.personalsoft.sqlproyect.model.db.UserEntity;
import com.personalsoft.sqlproyect.model.dto.UserDto;
import com.personalsoft.sqlproyect.service.UserService;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll() {
		return ResponseEntity.ok(userService.list());
	}

	@PostMapping
	@ResponseBody
	public UserEntity createU(@Valid @RequestBody UserDto user) {
		logger.info("¡Estamos creando un usuario aquí! : {}", user);
		return userService.create(user);
	}

	@PutMapping("/{id}")
	public UserEntity updateU(@RequestBody UserDto user, @PathVariable Integer id) throws Exception {		
		return userService.update(user, id);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserException.class)
	public Map<String, String> handleValidationExceptions(UserException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("mensaje", ex.getMessage());
		return errors;
	}

}

package com.personalsoft.sqlproyect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalsoft.sqlproyect.controller.UserController;
import com.personalsoft.sqlproyect.exception.UserException;
import com.personalsoft.sqlproyect.model.db.UserEntity;
import com.personalsoft.sqlproyect.model.dto.UserDto;
import com.personalsoft.sqlproyect.repository.UserRepository;
import com.personalsoft.sqlproyect.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MySqlProyectApplication.class)
@WebMvcTest({ UserController.class, UserService.class })
class MySqlProyectApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(MySqlProyectApplicationTests.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	UserController userController;

	@Autowired
	MockMvc mock;

	@MockBean
	UserRepository userRepository;

	UserDto userD;

	@BeforeEach
	void contextLoads() {
		userD = UserDto.builder().nombre("Lina David").email("ldavid@personalsoft.com").age(26).build();
	}

	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {
		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser()");

		// GIVEN
		UserEntity userRepositoryResponse = UserEntity.builder().id(1).nombre("Lina David")
				.email("ldavid@personalsoft.com").build();

		when(userRepository.save(any(UserEntity.class))).thenReturn(userRepositoryResponse);

		// WHEN
		MvcResult mvcRes = getResult(userD);
		String userJson = mvcRes.getResponse().getContentAsString();
		UserEntity userResponse = mapper.readValue(userJson, UserEntity.class);
		// userController.createU(userD);

		// THEN
		assertEquals(userD.getNombre(), userResponse.getNombre());
		assertEquals(userD.getEmail(), userResponse.getEmail());
		assertNotNull(userResponse.getId());
		assertTrue(userD.getAge() >= 18);
	}

	@Test
	void user_UT01_UpdateUserSuccess_ReturnOkAndAnUser() throws Exception {
		logger.info("user_UT01_UpdateUserSuccess_ReturnOkAndAnUser()");

		// GIVEN
		userD.setId(1);
		Optional<UserEntity> userRepositoryFind = Optional
				.of(UserEntity.builder().id(1).nombre("Lina Prueba").age(28).email("ldavid@personalsoft.com").build());
		when(userRepository.findById(anyInt())).thenReturn(userRepositoryFind);

		UserEntity userRepositorySave = UserEntity.builder().id(1).nombre("Lina David").email("ldavid@personalsoft.com")
				.age(26).build();
		when(userRepository.save(any(UserEntity.class))).thenReturn(userRepositorySave);

		// WHEN
		MvcResult mvcRes = getResultPut(userD, userD.getId());
		String userJson = mvcRes.getResponse().getContentAsString();
		UserEntity userResponse = mapper.readValue(userJson, UserEntity.class);

		// THEN
		assertNotNull(userRepositoryFind);
		assertTrue(userRepositoryFind.get().getAge() >= 25);
		assertNotNull(userResponse);
		assertEquals(userResponse.getNombre(), userD.getNombre());
		assertEquals(userResponse.getAge(), userD.getAge());
	}

	@Test
	void user_UT02_UpdateUserException_Age() {
		logger.info("user_UT02_UpdateUserException_Age()");

		// GIVEN
		userD.setId(1);
		Optional<UserEntity> userRepositoryFind = Optional
				.of(UserEntity.builder().id(1).nombre("Lina Prueba").age(15).email("ldavid@personalsoft.com").build());
		when(userRepository.findById(anyInt())).thenReturn(userRepositoryFind);

		// WHEN
		Exception exception = assertThrows(UserException.class, () -> userController.updateU(userD, userD.getId()));

		// THEN
		assertEquals("El usuario en base de datos es menor de 25 años y no se puede editar", exception.getMessage());
	}
	
	@Test
	void user_UT03_UpdateUserException_UserExist() {
		logger.info("user_UT02_UpdateUserException_UserExist()");

		// GIVEN
		userD.setId(1);
		Optional<UserEntity> userRepositoryFind = Optional
				.of(UserEntity.builder().id(1).nombre("Lina David").email("ldavid@personalsoft.com")
						.age(26).build());
		when(userRepository.findById(anyInt())).thenReturn(userRepositoryFind);

		// WHEN
		Exception exception = assertThrows(UserException.class, () -> userController.updateU(userD, userD.getId()));

		// THEN
		assertEquals("El usuario que quiere modificar es el mismo que está guardado", exception.getMessage());
	}

	private MvcResult getResult(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(
				post("/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
				.andReturn();
	}

	private MvcResult getResultPut(UserDto requestObject, Integer id) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(put("/".concat(id.toString())).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
	}

}

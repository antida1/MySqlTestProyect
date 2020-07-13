package com.personalsoft.sqlproyect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalsoft.sqlproyect.exception.UserException;
import com.personalsoft.sqlproyect.model.db.UserEntity;
import com.personalsoft.sqlproyect.model.dto.UserDto;
import com.personalsoft.sqlproyect.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<UserEntity> list() {
		return (List<UserEntity>) userRepository.findAll();
	}

	public UserEntity create(UserDto user) {
		UserEntity userE = new UserEntity();
		userE.setEmail(user.getEmail());
		userE.setNombre(user.getNombre());

		return userRepository.save(userE);
	}

	public UserEntity update(UserDto user, Integer id) throws UserException{
		UserEntity userBd = userRepository.findById(id).orElse(null);
		if (userBd != null) {

			if (userBd.getAge() < 25) {
				throw new UserException("El usuario en base de datos es menor de 25 y no se puede editar");
			} else {
				if(!userBd.getNombre().equals(user.getNombre()) || userBd.getAge() != user.getAge()) {
					userBd.setNombre(user.getNombre());
					userBd.setAge(user.getAge());
					userRepository.save(userBd);
				}else {
					throw new UserException("El usuario que quiere modificar es el mismo que está guardado");
				}
			}
		}
		return userBd;
	}
}

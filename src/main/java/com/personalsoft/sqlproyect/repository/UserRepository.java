package com.personalsoft.sqlproyect.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.personalsoft.sqlproyect.model.db.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{

}

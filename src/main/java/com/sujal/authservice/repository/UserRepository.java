package com.sujal.authservice.repository;

import com.sujal.authservice.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo, Integer> {

     UserInfo findByUsername(String username);
}

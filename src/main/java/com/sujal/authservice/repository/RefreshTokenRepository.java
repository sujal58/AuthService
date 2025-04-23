package com.sujal.authservice.repository;

import com.sujal.authservice.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {


    Optional<RefreshToken> findByToken(String token);


}

package com.sujal.authservice.service;


import com.sujal.authservice.entities.UserInfo;
import com.sujal.authservice.model.UserInfoDto;
import com.sujal.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found......");
        }
         return new CustomUserDetails(user);

    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        try{
            if(Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))){
                return false;
            }
            userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
            String userId = UUID.randomUUID().toString();

            UserInfo userToSave = new UserInfo(userId,userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());
            userRepository.save(userToSave);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }
}

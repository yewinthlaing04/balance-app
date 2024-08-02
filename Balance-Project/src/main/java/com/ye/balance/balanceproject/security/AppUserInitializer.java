package com.ye.balance.balanceproject.security;

import com.ye.balance.balanceproject.model.domain.entity.User;
import com.ye.balance.balanceproject.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppUserInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepository;

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void initializedUser(){
        if ( userRepository.count() == 0 ) {
            User user = new User();
            user.setName("Admin");
            user.setLoginId("admin");
            user.setRole(User.Role.Admin);
            user.setActive(true);
            user.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(user);
        }
    }
}

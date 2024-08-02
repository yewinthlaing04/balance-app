package com.ye.balance.balanceproject.model.service;

import com.ye.balance.balanceproject.model.BalanceAppException;
import com.ye.balance.balanceproject.model.domain.entity.User;
import com.ye.balance.balanceproject.model.domain.form.ChangePasswordForm;
import com.ye.balance.balanceproject.model.domain.form.SignUpForm;
import com.ye.balance.balanceproject.model.domain.vo.UserVo;
import com.ye.balance.balanceproject.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpForm form) {

        // in parameter that is userform , so we need to switch from userfrom to user entity
        //  var user = new User();

        //  user.setName(form.getName());
        //  user.setPassword(passwordEncoder.encode(form.getPassword()));
        //  user.setActive(true);
        //  user.setLoginId(form.getLoginId());
        //  user.setRole(User.Role.Member);

        form.setPassword(passwordEncoder.encode(form.getPassword()));

        userRepo.save(new User(form));
    }

    public UserVo findByLoginId(String user) {

        return userRepo.findOneByLoginId(user).map(UserVo::new).orElseThrow();

    }

    @Transactional
    public void updateContact(String username , String email, String phone) {

        userRepo.findOneByLoginId(username)
                .ifPresent( user -> {
                    user.setEmail(email);
                    user.setPhone(phone);
                });
    }

    public List<UserVo> search(Boolean status, String name, String phone) {

        // user list must be member role
        Specification<User> spec =  (root , query , builder ) -> builder.equal(root.get("role"), User.Role.Member);

        if ( status != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("active"), status));
        }

        if (StringUtils.hasLength(name)){
            spec = spec.and((root,query,builder) -> builder.like(builder.lower(root.get("name")),
                    name.toLowerCase().concat("%")));
        }

        if ( StringUtils.hasLength(phone)){
            spec= spec.and((root,query,builder) -> builder.like(root.get("phone"),phone.concat("%")));
        }

        /*
        findAll = user entity list
        stream
        map to UserVo from user
        toList : return type is list
         */
        return userRepo.findAll(spec)
                .stream()
                .map(UserVo::new)
                .toList();
    }

    @Transactional
    public void changeStatus(int id,  boolean status) {
        userRepo.findById(id).ifPresent( user -> user.setActive(status));
    }

    @Transactional
    public void changePassword(ChangePasswordForm changePasswordForm) {

        if (!StringUtils.hasLength(changePasswordForm.getNewPassword())){
            throw new BalanceAppException("Please enter new password");
        }

        if (!StringUtils.hasLength(changePasswordForm.getOldPassword())){
            throw new BalanceAppException("Please enter old password");
        }

        if ( !changePasswordForm.getOldPassword().equals(changePasswordForm.getNewPassword())){
            throw new BalanceAppException("Please enter different password with old password");
        }

        var user = userRepo.findOneByLoginId(changePasswordForm.getLoginId()).orElseThrow();

        if (!passwordEncoder.matches(changePasswordForm.getOldPassword() , user.getPassword())){
            throw new BalanceAppException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));

        userRepo.save(user);
    }
}

package com.ye.balance.balanceproject.security;


import com.ye.balance.balanceproject.model.domain.entity.UserAccessLog;
import com.ye.balance.balanceproject.model.repo.UserAccessLogRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class BalanceAppAccessListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAccessLogRepo userAccessLogRepo;

    @EventListener
    @Transactional
    void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent){
        var time = LocalDateTime.ofInstant
                (new Date(authenticationSuccessEvent.getTimestamp()).toInstant(),
                        ZoneId.systemDefault());

        var username = authenticationSuccessEvent.getAuthentication().getName();
        log.info("Successfully logged in user: " + username + " at " + time);

        userAccessLogRepo.save(new UserAccessLog(username , UserAccessLog.AccessType.Signin , time));
    }

    @EventListener
    @Transactional
    void onFailure(AbstractAuthenticationFailureEvent abstractAuthenticationFailureEvent){
        var time = LocalDateTime.ofInstant(
                new Date(abstractAuthenticationFailureEvent.getTimestamp()).toInstant(),ZoneId.systemDefault()
        );
        var username = abstractAuthenticationFailureEvent.getAuthentication().getName();
        log.info("Failure logged in user: " + username + " at " + time +
                abstractAuthenticationFailureEvent.getException().getMessage());

        userAccessLogRepo.save(new UserAccessLog(username , UserAccessLog.AccessType.Error
                ,
                time , abstractAuthenticationFailureEvent.getException().getMessage()));
    }

    // to know who , when signout

    @EventListener
    @Transactional
    void onSessionDestroyed(HttpSessionDestroyedEvent event ){
        event.getSecurityContexts().stream().findAny()
                .ifPresent( auth ->  {
                        var time = LocalDateTime.ofInstant(
                                new Date(event.getTimestamp()).toInstant(),ZoneId.systemDefault());
                        var username = auth.getAuthentication().getName();
                        log.info("Session sign out: " + username + " at " + time);
                        userAccessLogRepo.save(new UserAccessLog( username , UserAccessLog.AccessType.Signout , time));
                });

    }
}

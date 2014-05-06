package com.elitethought.service;

import com.elitethought.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by chenm on 6/05/2014.
 */
@Service
public interface UserService extends UserDetailsService {
    public void initialize();
    public Account save(Account account);
    public void signin(Account account);
    public Account findAccountByEmail(String email);
}

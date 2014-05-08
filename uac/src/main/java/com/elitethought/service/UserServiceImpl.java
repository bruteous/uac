package com.elitethought.service;

import com.elitethought.entity.Account;
import com.elitethought.entity.Role;
import com.elitethought.entity.RoleEnum;
import com.elitethought.repository.RoleRepository;
import com.elitethought.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

    @Autowired
   	private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

	@PostConstruct	
	public void initialize() {
        if (userRepository.findAccountByEmail("user") == null ) {
            userRepository.save(new Account("user", passwordEncoder.encode("demo"), new Role(RoleEnum.ROLE_USER.toString())));
        }
        if (userRepository.findAccountByEmail("admin") == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(RoleEnum.ROLE_ADMIN.toString()));
            userRepository.save(new Account("admin", passwordEncoder.encode("admin"), roles));
        }
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = userRepository.findAccountByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, createAuthority(account));
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), createAuthority(account));
	}

	private Collection<GrantedAuthority> createAuthority(Account account) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role :  account.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(authority);
        }
		return authorities;
	}

    public Account findAccountByEmail(String email) {
        return userRepository.findAccountByEmail(email);
    }

    public Account save(Account account) {
        Set<Role> roles = new HashSet<>();
        for (Role role : account.getRoles()) {
            Role tmpRole = roleRepository.findRoleByRoleName(role.getRoleName());
            if ( tmpRole != null) {
                roles.add(tmpRole);
            } else {
                roles.add(role);
            }
        }
        account.setRoles(roles);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return userRepository.save(account);
    }
}

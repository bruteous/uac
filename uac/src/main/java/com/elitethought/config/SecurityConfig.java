package com.elitethought.config;

import com.elitethought.repository.UserRepository;
import com.elitethought.service.SimpleSocialUserDetailsService;
import com.elitethought.service.UserService;
import com.elitethought.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebSecurity
//@ImportResource(value = "classpath:spring-security-context.xml")
class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    private UserRepository userRepository;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
           			.formLogin()
           				.loginPage("/signin")
           				.failureUrl("/signin?error=1")
           			.and()
           				.logout()
           					.logoutUrl("/logout")
                            .logoutSuccessUrl("/singnin")
           					.deleteCookies("JSESSIONID")
           			.and()
           				.authorizeRequests()
           					.antMatchers("/", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
           					.antMatchers("/**").authenticated()
           			.and()
           				.rememberMe().rememberMeServices(rememberMeServices())
                    .and()
                        .csrf()
           			.and()
           				.apply(new SpringSocialConfigurer());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());
    }

    @Bean
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		return new TokenBasedRememberMeServices("remember-me-key", userService());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
	}

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
           return new SimpleSocialUserDetailsService(userDetailsService());
    }

}
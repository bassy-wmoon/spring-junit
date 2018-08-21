package io.github.bassy.wmoon.app.service;

import io.github.bassy.wmoon.app.model.Account;
import io.github.bassy.wmoon.app.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

	private AccountRepository accountRepository;

	public LoginService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.selectByUsername(username);
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(""));
		return new User(account.getUsername(), account.getPassword(), authorities);
	}
}

package io.github.bassy.wmoon.app.repository;

import io.github.bassy.wmoon.app.model.Account;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public AccountRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public Account selectByUsername(String username) {

		return namedParameterJdbcTemplate.queryForObject(
				"select username, password, role from account", new MapSqlParameterSource("username", username), Account.class);
	}
}

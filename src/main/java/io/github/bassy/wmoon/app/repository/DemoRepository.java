package io.github.bassy.wmoon.app.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DemoRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public DemoRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public int count() {
		return namedParameterJdbcTemplate.queryForObject(
				"select count(1) from user", new MapSqlParameterSource(), Integer.class);
	}
}

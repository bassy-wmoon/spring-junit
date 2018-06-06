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
		int count = namedParameterJdbcTemplate.queryForObject(
				"select count(1) from test", new MapSqlParameterSource(), Integer.class);
		
		return count;
	}
}

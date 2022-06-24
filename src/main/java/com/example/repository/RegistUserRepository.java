package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.RegistUrl;
import com.example.domain.User;

@Repository
public class RegistUserRepository {
	private static final RowMapper<RegistUrl> REGIST_URL_ROW_MAPPER = new BeanPropertyRowMapper<>(RegistUrl.class);
	private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<RegistUrl> findRegistUrl(String email) {
		String sql = "SELECT * FROM regist_url WHERE mail_address = :mailAddress";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<RegistUrl> registUrlList = template.query(sql, param, REGIST_URL_ROW_MAPPER);
		if (registUrlList.size() == 0) {
			return null;
		}
		System.out.println(registUrlList);
		return registUrlList;
	}

	public List<User> findByMailAddress(String email) {
		String sql = "SELECT * FROM users WHERE mail_address = :mailAddress";

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		System.out.println(userList);
		return userList;
	}

	public void insertRegistUrl(RegistUrl registUrl) {
		String sql = "INSERT INTO regist_url(mail_address, unique_key, regist_date, deleted)"
				+ " VALUES(:mailAddress, :uniqueKey, :registDate, :deleted);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(registUrl);
		template.update(sql, param);
	}
}

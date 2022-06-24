package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.RegistUrl;
import com.example.domain.User;
import com.example.repository.RegistUserRepository;

@Service
public class RegistUserService {
	@Autowired
	private RegistUserRepository repository;

	public List<RegistUrl> findRegistUrl(String email) {
		List<RegistUrl> registUrlList = repository.findRegistUrl(email);
		return registUrlList;
	}

	public List<User> findByMailAddress(String email) {
		List<User> userList = repository.findByMailAddress(email);
		return userList;
	}

	public void insertRegistUrl(RegistUrl registUrl) {
		repository.insertRegistUrl(registUrl);
	}
}

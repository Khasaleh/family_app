package com.cybersolution.imageinterface.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.repository.UserRepository;
import com.cybersolution.imageinterface.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repo;

	@Override
	public User getById(Integer id) {
		return repo.findById(id).get();
	}

}

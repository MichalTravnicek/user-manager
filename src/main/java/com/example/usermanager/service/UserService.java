package com.example.usermanager.service;

import org.springframework.stereotype.Service;

import com.example.usermanager.model.EntityMapper;
import com.example.usermanager.model.User;
import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.persistence.UsersDao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UsersDao usersDao;

	public boolean createUser(UserJson userJson){
		User user = EntityMapper.INSTANCE.userJsonToUser(userJson);
		final User createdUser = usersDao.createOne(user);
		return createdUser.getId() > 0;
	}

	public UserJson getUser(String email){
		User user = usersDao.getOneByEmail(email);
		return EntityMapper.INSTANCE.userToUserJson(user);
	}

}

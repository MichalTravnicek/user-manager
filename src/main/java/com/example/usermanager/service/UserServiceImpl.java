package com.example.usermanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.usermanager.model.EntityMapper;
import com.example.usermanager.model.User;
import com.example.usermanager.model.rest.UserJson;
import com.example.usermanager.persistence.UsersDao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UsersDao usersDao;

	@Override
	public boolean createUser(UserJson userJson){
		User user = EntityMapper.INSTANCE.userJsonToUser(userJson);
		final User createdUser = usersDao.createOne(user);
		return createdUser.getId() > 0;
	}

	@Override
	public UserJson getUser(String email){
		User user = usersDao.getOneByEmail(email);
		return EntityMapper.INSTANCE.userToUserJson(user);
	}

	@Override
	public boolean updateUser(UserJson userJson){
		User user = EntityMapper.INSTANCE.userJsonToUser(userJson);
		int result = usersDao.updateOne(user);
		return result > 0;
	}

	@Override
	public boolean deleteUser(String email){
		int result = usersDao.deleteOne(email);
		return result > 0;
	}

	@Override
	public List<UserJson> getAllUsers(){
		List<User> users = usersDao.getAll();
		return EntityMapper.INSTANCE.mapToJson(users);
	}

}

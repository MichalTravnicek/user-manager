package com.example.usermanager.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public UserJson createUser(UserJson userJson){
		User user = EntityMapper.INSTANCE.userJsonToUser(userJson);
		final User createdUser = usersDao.createOne(user);
		return EntityMapper.INSTANCE.userToUserJson(createdUser);
	}

	@Override
	public UserJson getUser(String email){
		User user = usersDao.getOneByEmail(email);
		return EntityMapper.INSTANCE.userToUserJson(user);
	}

	@Override
	public UserJson getUserById(UUID uuid){
		User user = usersDao.getOneByUuid(uuid);
		Logger.getLogger(this.toString()).log(Level.INFO,"Get:" + user);
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
	public boolean deleteUserById(final UUID uuid) {
		int result = usersDao.deleteOneById(uuid);
		return result > 0;
	}

	@Override
	public List<UserJson> getAllUsers(){
		List<User> users = usersDao.getAll();
		return EntityMapper.INSTANCE.mapToJson(users);
	}

	@Override
	public List<UserJson> getUsersByParams(UserJson params){
		var converted = EntityMapper.INSTANCE.mapParameters(params);
		List<User> users = usersDao.searchByFuzzy(converted);
		return EntityMapper.INSTANCE.mapToJson(users);
	}

}

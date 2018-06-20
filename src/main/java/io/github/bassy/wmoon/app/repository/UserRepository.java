package io.github.bassy.wmoon.app.repository;

import io.github.bassy.wmoon.app.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRepository {

	void insertUser(User user);

	List<User> selectUsers();

	User selectOne(int id);
}

package com.test.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
	List<User> findByIdOrNameOrPassword(Long id,String name,String password);
}

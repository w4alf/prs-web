package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.prs.business.Request;
import com.prs.business.User;

public interface RequestRepository extends CrudRepository<Request, Integer> {

		List<Request> findByStatusEqualsAndUserNot(String status, User user);
} 

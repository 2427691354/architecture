package com.lixing.dao;

import com.lixing.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User,String> {
     User findByUserName(String name);

     User findByUserNameAndPassword(String name,String pwd);
}

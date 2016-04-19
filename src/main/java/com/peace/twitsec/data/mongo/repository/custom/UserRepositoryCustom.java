package com.peace.twitsec.data.mongo.repository.custom;

import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface UserRepositoryCustom {

    User loadById(Long id);

    User findByUsernameAndPassword(String username, String password);
    
    User findByUsername(String username);
    
    User findByOneTimeToken(String oneTimeToken);
    
    boolean deleteUser(String username);
}

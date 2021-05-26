package org.extvos.auth.service.impl;

import org.extvos.auth.entity.User;
import org.extvos.auth.mapper.UserMapper;
import org.extvos.auth.service.UserService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mingcai SHEN
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper myMapper;

    @Override
    public UserMapper getMapper() {
        return myMapper;
    }
}

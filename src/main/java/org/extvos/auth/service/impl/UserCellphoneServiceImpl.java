package org.extvos.auth.service.impl;

import org.extvos.auth.entity.UserCellphone;
import org.extvos.auth.mapper.UserCellphoneMapper;
import org.extvos.auth.service.UserCellphoneService;
import org.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mingcai SHEN
 */
@Service
public class UserCellphoneServiceImpl extends BaseServiceImpl<UserCellphoneMapper, UserCellphone> implements UserCellphoneService {
    @Autowired
    private UserCellphoneMapper myMapper;

    @Override
    public UserCellphoneMapper getMapper() {
        return myMapper;
    }
}

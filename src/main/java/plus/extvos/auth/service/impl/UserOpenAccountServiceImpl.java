package plus.extvos.auth.service.impl;

import plus.extvos.auth.entity.UserOpenAccount;
import plus.extvos.auth.mapper.UserOpenAccountMapper;
import plus.extvos.auth.service.UserOpenAccountService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户第三方开放账户关联账号
 * @author Mingcai SHEN
 */
@Service
public class UserOpenAccountServiceImpl extends BaseServiceImpl<UserOpenAccountMapper, UserOpenAccount> implements UserOpenAccountService {
    @Autowired
    private UserOpenAccountMapper myMapper;
    
    @Override
    public UserOpenAccountMapper getMapper() {
        return myMapper;
    }
}

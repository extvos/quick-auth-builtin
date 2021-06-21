package plus.extvos.auth.service.impl;

import plus.extvos.auth.entity.UserWechatAccount;
import plus.extvos.auth.mapper.UserWechatAccountMapper;
import plus.extvos.auth.service.UserWechatAccountService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户微信关联账号
 * @author Mingcai SHEN
 */
@Service
public class UserWechatAccountServiceImpl extends BaseServiceImpl<UserWechatAccountMapper, UserWechatAccount> implements UserWechatAccountService {
    @Autowired
    private UserWechatAccountMapper myMapper;
    
    @Override
    public UserWechatAccountMapper getMapper() {
        return myMapper;
    }
}

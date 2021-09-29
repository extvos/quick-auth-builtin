package plus.extvos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import plus.extvos.auth.entity.UserEmail;
import plus.extvos.auth.mapper.UserEmailMapper;
import plus.extvos.auth.service.UserEmailService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

/**
 * @author shenmc
 */
public class UserEmailServiceImpl extends BaseServiceImpl<UserEmailMapper, UserEmail> implements UserEmailService {
    @Autowired
    private UserEmailMapper myMapper;

    @Override
    public UserEmailMapper getMapper() {
        return myMapper;
    }
}

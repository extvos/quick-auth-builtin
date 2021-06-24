package plus.extvos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.extvos.auth.entity.UserCellphone;
import plus.extvos.auth.mapper.UserCellphoneMapper;
import plus.extvos.auth.service.UserCellphoneService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;

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

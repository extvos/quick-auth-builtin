package plus.extvos.auth.service.impl;

import plus.extvos.auth.dto.UserCellphone;
import plus.extvos.auth.mapper.UserCellphoneMapper;
import plus.extvos.auth.service.UserCellphoneService;
import plus.extvos.restlet.service.impl.BaseServiceImpl;
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

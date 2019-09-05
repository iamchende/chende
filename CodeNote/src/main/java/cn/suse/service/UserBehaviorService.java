package cn.suse.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service("userBehaviorService")
public class UserBehaviorService extends BehaviorLogService{

    @Override
    protected void setBeforeData4Update() {

    }

    @Override
    protected void setBeforeData4Delete() {

    }

    @Override
    protected void setAfterData4Insert() {

    }

    @Override
    protected void setAfterData4Update() {

    }
}

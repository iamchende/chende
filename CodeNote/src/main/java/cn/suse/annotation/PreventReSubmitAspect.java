package cn.suse.annotation;

import cn.suse.basic.BaseResultUtils;
import cn.suse.constants.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
@Slf4j
public class PreventReSubmitAspect {

    private PropertyUtilsBean beanUtil = new PropertyUtilsBean();

    public Object doAround(ProceedingJoinPoint pjp, PreventReSubmit submit) throws Throwable {
        Signature signature = pjp.getSignature();
        Object[] args = pjp.getArgs();
        if (args == null || args.length == 0) {
            //没有找到参数
            return pjp.proceed();
        }
        //根据入参获取一个便于判断的唯一表示
        String key = getUniqueKey(args[0], submit);
        if(null != key){
            //尝试枷锁，可以用表，redis，zookeeper等分布式锁。
            if(true){
                try{
                    return pjp.proceed();
                } finally{
                    //一定记得释放锁
                }
            }else{
                //枷锁失败，说明此请求是重复请求
                return BaseResultUtils.fail(ErrorCodeEnum.CMN_20003.getCode(), "请勿重复操作！", null);
            }
        }else{
            return pjp.proceed();
        }
    }

    /**
     * 根据入参获取一个便于判断的唯一表示
     *
     * @param args
     * @param submit
     * @return
     */
    private String getUniqueKey(Object args, PreventReSubmit submit) {
        String[] checkFields = submit.checkFields();
        StringBuilder key = new StringBuilder();
        if (null != checkFields && checkFields.length > 0) {
            for (String field : checkFields) {
                if (StringUtils.isNotBlank(field)) {
                    Object fieldValue = this.getParam(args, field);
                    if (null == fieldValue) {
                        continue;
                    }
                    if (fieldValue instanceof String) {
                        key.append("_").append((String) fieldValue);
                    } else {
                        key.append("_").append(fieldValue.toString());
                    }
                }
            }
        }
        if (key.length() > 0) {
            return submit.prefix() + key.toString();
        }
        return null;
    }

    private Object getParam(Object args, String field) {
        if (args == null || StringUtils.isBlank(field)) {
            return null;
        }
        try {
            return beanUtil.getProperty(args, field);
        } catch (Exception e) {
            log.error("PropertyUtilsBean.getProperty 获取参数失败", e);
            return null;
        }
    }
}
package cn.suse.basic;

import cn.suse.constants.ErrorCodeEnum;

public class BaseResultUtils<T> {

    /**
     * 构建成功返回结果
     */
    static public <T> BaseResult<T> success(T returnObj) {
        return success(returnObj, ErrorCodeEnum.SYS_RUNTIME_ERROR.getDesc());
    }

    static public <T> BaseResult<T> success(T returnObj, String successMessage) {
        BaseResult<T> result = new BaseResult<T>();
        result.setSuccess(returnObj);
        result.setMessage(successMessage);
        return result;
    }

    /**
     * 构建业务失败返回结果
     */
    static public <T> BaseResult<T> fail(String resultCode, String resultMsg, T returnObj) {
        BaseResult<T> result = new BaseResult<T>();
        result.setCode(resultCode);
        result.setMessage(resultMsg);
        result.setResult(returnObj);
        return result;
    }

}

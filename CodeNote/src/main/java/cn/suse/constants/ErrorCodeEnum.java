package cn.suse.constants;

public enum ErrorCodeEnum{

    SYS_RUNTIME_ERROR("0", "业务处理成功"),

    /**
     * 10000 - 19999表示业务警告
     */
    CMN_10000("10000", ""),
    CMN_10001("10001", "当前联系人电话非本地号码！"),

    /**
     * 通用错误(20000 - 29999)
     */
    CMN_20001("20001", "没有权限访问该资源"),
    CMN_20002("20002", "资源不存在"),
    CMN_20003("20003", "重复请求"),
    /**
     * 通用系统错误代码(40001 - 40002)
     */
    CMN_40001("40001", "系统维护中."),
    CMN_40002("40002", "系统繁忙"),
    CMN_40003("40003", "系统异常"),
    /**
     * 理赔自定义错误(30129 - )
     */
    CMN_80001("40003", "业务逻辑错误");


    private String code;
    private String desc;

    private ErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ErrorCodeEnum getEnumByCode(String code) {

        for (ErrorCodeEnum statusEnum : ErrorCodeEnum.values()) {
            if (statusEnum.getCode().equals(code))
                return statusEnum;
        }
        return null;
    }
}

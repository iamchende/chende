package cn.suse.constants;

/**
 * 不同的业务场景有不同的数据类型，名称，需求。
 * 让特定的Service记录特定的日志
 */
public enum BehaviorLogServiceEnum {

    USER("userBehaviorService", "用户信息记录Service"),
    BBB("bbbService", "权限信息记录Service");

    private String code;
    private String desc;

    private BehaviorLogServiceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BehaviorLogServiceEnum getEnumByCode(String code) {

        for (BehaviorLogServiceEnum statusEnum : BehaviorLogServiceEnum.values()) {
            if (statusEnum.getCode().equals(code))
                return statusEnum;
        }
        return null;
    }
}

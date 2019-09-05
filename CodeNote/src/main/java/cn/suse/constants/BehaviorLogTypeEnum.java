package cn.suse.constants;

public enum BehaviorLogTypeEnum {

    QUERY("query", "查询"),
    INSERT("insert", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),
    BATCH_DELETE("batch_delete", "批量删除"),
    BATCHIMPORT("batch_import", "导入"),
    BATCHEXPORT("batch_export", "导出");

    private String code;
    private String desc;

    private BehaviorLogTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BehaviorLogTypeEnum getEnumByCode(String code) {

        for (BehaviorLogTypeEnum statusEnum : BehaviorLogTypeEnum.values()) {
            if (statusEnum.getCode().equals(code))
                return statusEnum;
        }
        return null;
    }
}

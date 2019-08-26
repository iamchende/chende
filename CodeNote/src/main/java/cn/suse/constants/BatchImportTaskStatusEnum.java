package cn.suse.constants;

public enum BatchImportTaskStatusEnum {
    PENDING(0, "待处理"),
    PROCESSING(1, "处理中"),
    SUCCESS(2, "处理成功"),
    FAIL(3, "处理失败");

    private Integer code;
    private String name;

    BatchImportTaskStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BatchImportTaskStatusEnum getEnumByCode(Integer code) {
        for (BatchImportTaskStatusEnum statusEnum : BatchImportTaskStatusEnum.values()) {
            if (statusEnum.getCode().equals(code))
                return statusEnum;
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return this.name;
    }
}

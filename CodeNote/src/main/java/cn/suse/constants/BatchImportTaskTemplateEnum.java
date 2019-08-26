package cn.suse.constants;

/**
 * 批量导入模板文件
 */
public enum BatchImportTaskTemplateEnum {

	AUTOBATCHCLOSE_POLICY_CONFIG(1, "/excelTemplate/autoBatchClosePolicyConfigTemplate.xlsx", "XXXX配置导入模板.xlsx");
    
    private Integer docId;
    
    private String docPath;

    private String docDesc;

    private BatchImportTaskTemplateEnum(Integer docId, String docPath, String docDesc) {
        this.docId = docId;
        this.docPath = docPath;
        this.docDesc = docDesc;
    }

    public static BatchImportTaskTemplateEnum getEnumByCodeId(Integer docId) {
        for (BatchImportTaskTemplateEnum an : BatchImportTaskTemplateEnum.values()) {
            if (an.getDocId().equals(docId)) {
                return an;
            }
        }
        return null;
    }

    public final Integer getDocId() {
        return docId;
    }

    public final String getDocPath() {
        return docPath;
    }

    public final String getDocDesc() {
        return docDesc;
    }

}

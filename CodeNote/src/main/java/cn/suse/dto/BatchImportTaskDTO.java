package cn.suse.dto;

import lombok.Data;

@Data
public class BatchImportTaskDTO {

    /**
     * 任务名称
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务状态：0-待处理; 1-处理中; 2-处理成功; 3-处理失败
     */
    private Integer status;

    /**
     * 备份路径
     */
    private String filePath;

    /**
     * 备注
     */
    private String remark;
    /**
     * 上传者
     */
    private String creator;
    /**
     * 修改者
     */
    private String modifier;
    /**
     * 当前任务总记录数
     */
    private Integer recordNum;
    /**
     * 当前任务失败的记录数
     */
    private Integer failNum;
    /**
     * 当前任务成功的记录数
     */
    private Integer successNum;
}

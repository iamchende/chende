package cn.suse.task;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.suse.basic.BaseResult;
import cn.suse.constants.BatchImportTaskStatusEnum;
import cn.suse.dto.BatchImportTaskDTO;
import cn.suse.service.BatchImportTaskService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigBatchImportTask implements Runnable {

    private static final int              SHEET_INDEX = 1;

    private static final int              START_ROW   = 2;

    private Long                          taskId;

    private String                        modifier;

    private BatchImportTaskService batchImportTaskService;
    
    

    public ConfigBatchImportTask(Long taskId, String modifier, BatchImportTaskService batchImportTaskService) {
		super();
		this.taskId = taskId;
		this.modifier = modifier;
		this.batchImportTaskService = batchImportTaskService;
	}
    

	public ConfigBatchImportTask() {
		super();
	}

	private void removeBlank(List<?> dataList) {
        //去除空白行(只有序号，没有任何业务数据)
        CollectionUtils.filter(dataList, new Predicate() {
            public boolean evaluate(Object object) {
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        if (!field.getName().equals("serialVersionUID") && !field.getName().equals("seq")
                                && null != field.get(object) && StringUtils.isNotBlank(field.get(object).toString())) {
                            return true;
                        }
                    } catch (Exception e) {
                        log.warn("数据{}可能是不包含业务数据的空白记录，将被忽略。", JSON.toJSONString(object), e);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void run() {
        log.info("导入配置：taskId={}", taskId);
        //保存解析后的数据
        List<ConfigBatchImportTemplate> configs = null;
        try {
            BaseResult<BatchImportTaskDTO> taskById = batchImportTaskService.getTaskById(taskId);
            String filePath = taskById.getResult().getFilePath();
            //从OSS下载用户上传的批量配置文件
            try (InputStream in = downloadFile(filePath)) {//对于实现了closeable接口的资源可以这么写，可以自动关闭资源。
                if (in != null) {
                    //更新任务为正在运行
                    updateTask(taskId, null, null, null, BatchImportTaskStatusEnum.PROCESSING, null);
                    //解析文件，解析一行，校验一行。并增加一列保存错误信息。
                    //configs = convert(in);
                } else {
                    //更新任务状态为失败
                    log.warn("下载文件失败，返回文件流为空。taskId：{}，文件filePath：{}", taskId, filePath);
                    updateTask(taskId, null, null, null, BatchImportTaskStatusEnum.FAIL, "下载文件失败");
                    return;
                }
            }
            
            //对解析后的数据进行校验相关操作
            boolean hasErrorInfo = true;
            List<String> errorList = null;
            

            //异常说明文件写回OSS
            int totalNum = configs.size();
            if (hasErrorInfo) {
                log.info("批量配置异常：taskiId={}", taskId, JSON.toJSONString(errorList));
                String errorFilePath = "";
                int index = filePath.indexOf(".");
                if (-1 == index) {
                    errorFilePath = filePath + "_" + "error";
                } else {
                    errorFilePath = filePath.substring(0, index) + "_error" + filePath.substring(index);
                }
                //将文件又上传到指定地方，让前端去获取
                updateTask(taskId, errorFilePath, null, null, BatchImportTaskStatusEnum.FAIL, "配置文件校验未通过");
            } else {
                //入库，批量添加配置
                BaseResult<Void> batchAddResult = null;
                if (batchAddResult.success()) {
                    updateTask(taskId, null, totalNum, 0, BatchImportTaskStatusEnum.SUCCESS, null);
                } else {
                    updateTask(taskId, null, totalNum, totalNum, BatchImportTaskStatusEnum.FAIL, "批量配置时失败：" + batchAddResult.getMessage());
                }
            }
        } catch (Exception e) {
            //更新任务状态为失败
            log.error("ConfigBatchImportTask任务异常。taskId：{}", taskId, e);
            updateTask(taskId, null, null, null, BatchImportTaskStatusEnum.FAIL, e.getMessage());
        }
    }

    private InputStream downloadFile(String filePath) {
		// 把文件下载下来
		return null;
	}
    
    private void updateTask(Long id, String validatePath, Integer totalNum, Integer failNum, BatchImportTaskStatusEnum status, String remark) {
        BatchImportTaskDTO updateDTO = new BatchImportTaskDTO();
        updateDTO.setTaskId(id);
        updateDTO.setFilePath(validatePath);
        updateDTO.setRecordNum(totalNum);
        updateDTO.setFailNum(failNum);
        updateDTO.setRemark(remark);
        updateDTO.setModifier(modifier);
        if (totalNum != null && failNum != null) {
            updateDTO.setSuccessNum(totalNum - failNum);
        }
        updateDTO.setStatus(status.getCode());
        batchImportTaskService.updateTaskById(updateDTO);
    }
}

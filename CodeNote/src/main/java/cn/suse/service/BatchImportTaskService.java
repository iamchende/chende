package cn.suse.service;

import cn.suse.cn.suse.basic.BaseResult;
import cn.suse.constants.BatchImportTaskStatusEnum;
import cn.suse.dto.BatchImportTaskDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BatchImportTaskService {
    public BaseResult<Long> createTask(BatchImportTaskDTO taskDTO) {
        log.info("createTask,params:{}", JSON.toJSONString(taskDTO));
        taskDTO.setStatus(BatchImportTaskStatusEnum.PENDING.getCode());//默认为待处理
        BaseResult<Long> result = new BaseResult<>();
        //将任务放到表里面保存起来。
        result.setSuccess(taskDTO.getTaskId());
        return result;
    }

    public void asyncImport(BatchImportTaskDTO taskDTO) {
        log.info("asyncImport,params:{}", JSON.toJSONString(taskDTO));
    }
}

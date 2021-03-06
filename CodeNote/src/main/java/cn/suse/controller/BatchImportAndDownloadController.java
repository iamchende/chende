package cn.suse.controller;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableSet;

import cn.suse.basic.BaseResult;
import cn.suse.basic.BaseResultUtils;
import cn.suse.basic.BusinessException;
import cn.suse.constants.BatchImportTaskTemplateEnum;
import cn.suse.constants.ErrorCodeEnum;
import cn.suse.dto.BatchImportTaskDTO;
import cn.suse.service.BatchImportTaskService;
import lombok.extern.slf4j.Slf4j;

@RestController("/batch")
@Slf4j
public class BatchImportAndDownloadController {

    @Autowired
    BatchImportTaskService batchImportTaskService;

    private static final ImmutableSet<String> FILES_SUPPORT = ImmutableSet.<String>builder().add("xlsx", "xls").build();
    /**
     * 下载模板文件
     */
    @RequestMapping(value = "/templateDoc/{docId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadTemplateFile(@PathVariable(value = "docId") Integer docId) {
        log.info("downloadTemplateFile,docId={}", docId);
        ResponseEntity<byte[]> resEntity = null;
        try {
            BatchImportTaskTemplateEnum enumByCodeId = BatchImportTaskTemplateEnum.getEnumByCodeId(docId);
            if (null == enumByCodeId) {
                log.warn("下载模板文件docId不存在！,docId={}", docId);
                return resEntity;
            }
            ClassPathResource res = new ClassPathResource(enumByCodeId.getDocPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(enumByCodeId.getDocDesc(), "UTF-8"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            try (InputStream inputStream = res.getInputStream()) {
                resEntity = new ResponseEntity<>(IOUtils.toByteArray(inputStream), headers, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.error("下载模板文件异常,docId={}", docId, e);
        }
        return resEntity;
    }
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public BaseResult<Long> preImport(@RequestPart("importFile") MultipartFile importFile, @RequestParam("sheet") int sheet) {
        log.info("文件上传开始");
        //文件后缀检查
        if (!isFileSupported(importFile)) {
            return BaseResultUtils.fail(ErrorCodeEnum.CMN_20001.getCode(), "导入文件只支持xlsx格式", null);
        }
        try {
        	//保存文件
        	
            BatchImportTaskDTO taskDTO = new BatchImportTaskDTO();
            taskDTO.setCreator("当前用户");
            taskDTO.setFilePath("/上传文件到OSS或者本地或者其他地方后得到的路径");
            BaseResult<Long> createTaskResult = batchImportTaskService.createTask(taskDTO);
            if (createTaskResult == null || createTaskResult.failed()) {
                throw new BusinessException(ErrorCodeEnum.CMN_20001, "生成导入任务失败");
            }
            log.info("task创建成功,id={}", createTaskResult.getResult());
            //避免数据过多导致解析超时，前端拿不到响应而中断请求，启用异步任务解析。
            batchImportTaskService.asyncImport(taskDTO);
            return createTaskResult;
        } catch (BusinessException e) {
        log.warn("import BusinessException", e);
        return BaseResultUtils.fail(e.getErrorCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            log.error("import Exception", e);
            return BaseResultUtils.fail(ErrorCodeEnum.CMN_40003.getCode(), "Exception", null);
        }
    }
    private boolean isFileSupported(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String suffix = fileName.substring(index + 1);
        return index > 0 && FILES_SUPPORT.contains(suffix);
    }
    /**
     * 前端周期调用此接口，以获取任务的实时状态
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BaseResult<BatchImportTaskDTO> getTaskById(@PathVariable(value = "id") Long id) {
        log.info("getTaskById, id={}", id);
        try {
            BaseResult<BatchImportTaskDTO> getTaskResult = batchImportTaskService.getTaskById(id);
            //为了减少存储内容，path属性可能存放的是相对路径，此处可以通过拼接OSS域名或其他前缀
            BatchImportTaskDTO taskDTO = getTaskResult.getResult();
            return getTaskResult;
        } catch (BusinessException e) {
            log.warn("getTaskById, id={}", id, e);
            return BaseResultUtils.fail(e.getErrorCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            log.error("getTaskById, id={}", id, e);
            return BaseResultUtils.fail(ErrorCodeEnum.CMN_40003.getCode(), null, null);
        }
    }
}

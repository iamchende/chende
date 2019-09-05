package cn.suse.service;

import cn.suse.constants.BehaviorLogServiceEnum;
import cn.suse.constants.BehaviorLogTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class BehaviorLogService {

    /**
     * 请求参数
     */
    protected Object                    args;

    /**
     * 操作类型
     */
    protected BehaviorLogTypeEnum    logType;
    /**
     * 方法执行前数据
     */
    protected String                    beforeJson;

    /**
     * 方法执行后数据
     */
    protected String                    afterJson;
    /**
     * 是否开启开关,可以在配置表或者配置文件中定义
     * 比如默认关闭，有配置才开启
     */
    public boolean                      recordSwitch;

    public String                       addrIp;
    /**
     * 根据特定的字段或内容是否发生变化确定是否需要记录日志
     */
    public boolean                      isChange;

    public void init(Object obj, BehaviorLogTypeEnum logType){
        /**
         * 加载配置项里面的开关信息
         */
        this.recordSwitch = true;
        this.logType = logType;
    }
    public void setBeforeData(){
        try {
            if (!recordSwitch){
                log.warn("操作不再执行");
                return;
            }
            //根据不同的操作类型，获取不同的beforeJson
            switch (logType){
                case INSERT:
                    break;
                case UPDATE:
                    this.setBeforeData4Update();
                    break;
                case DELETE:
                case BATCH_DELETE:
                    this.setBeforeData4Delete();
                    break;
            }
        }catch (Exception e){
            log.error("获取变更前的数据异常。",e);
        }
    }
    public void setAfterData(){
        try {
            if (!recordSwitch){
                log.warn("操作不再执行");
                return;
            }
            //根据不同的操作类型，获取不同的afterJson
            switch (logType){
                case INSERT:
                    this.setAfterData4Insert();
                    break;
                case UPDATE:
                    this.setAfterData4Update();
                    break;
                case DELETE:
                case BATCH_DELETE:
            }
        }catch (Exception e){
            log.error("获取变更后的数据异常。",e);
        }
    }
    protected abstract void setBeforeData4Update();

    protected abstract void setBeforeData4Delete();

    protected abstract void setAfterData4Insert();

    protected abstract void setAfterData4Update();

    public void saveDataChange(){
        //记录日志
    }
}

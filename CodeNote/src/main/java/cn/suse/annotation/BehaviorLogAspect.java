package cn.suse.annotation;

import cn.suse.service.BehaviorLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
public class BehaviorLogAspect {

    @Resource
    private ApplicationContext applicationContext;

    @Pointcut(value = "@annotation(cn.suse.annotation.BehaviorLog)")
    public void pointCut() {
        //定义一个切入点，仅此而已
    }
    public Object doAround(MethodInvocationProceedingJoinPoint pjp, BehaviorLog behaviorLog, HttpServletRequest request) throws Throwable {
        try {
            BehaviorLogService service = applicationContext.getBean(behaviorLog.behaviorLogService().getCode(), BehaviorLogService.class);
            service.init(pjp.getArgs()[0], behaviorLog.behaviorLogType());
            Object obj;
            if(service.isRecordSwitch()){
                service.setBeforeData();
                obj = pjp.proceed();
                service.setAfterData();
                addBehaviorLog(service,request);
            }else{
                obj = pjp.proceed();
            }
            return obj;
        } catch (Throwable e) {
            log.error("BehaviorLogAspect.doAround 操作日志记录异常。", e);
            throw e;
        }
    }

    private void addBehaviorLog(BehaviorLogService service,HttpServletRequest request){
        if(service.isChange){
            service.setAddrIp(getIpAddr(request));
            //异步记录日志
            service.saveDataChange();
        }
    }
    /**
     * 获取登录用户IP地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}

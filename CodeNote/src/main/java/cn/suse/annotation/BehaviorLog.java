package cn.suse.annotation;

import cn.suse.constants.BehaviorLogServiceEnum;
import cn.suse.constants.BehaviorLogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BehaviorLog {

    BehaviorLogTypeEnum behaviorLogType();

    BehaviorLogServiceEnum behaviorLogService();
}

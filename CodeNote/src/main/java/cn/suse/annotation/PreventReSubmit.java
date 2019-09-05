package cn.suse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreventReSubmit {
    /**
     * 多少秒内的同一个请求需要拦截
     */
    int expireInSeconds() default 30;

    /**
     * 根据那些字段来判断是同一个请求;也可以用Spring的El表达式来处理。
     */
    String[] checkFields() default {};

    /**
     * 避免不同的业务场景出现相同的参数，给个前缀以示区分
     */
    String prefix() default "";
}

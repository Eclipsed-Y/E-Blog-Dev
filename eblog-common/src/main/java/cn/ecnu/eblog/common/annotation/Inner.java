package cn.ecnu.eblog.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})  // 接口可以标注在方法上，也可以在类、接口、枚举类上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {
    /**
     * 标记内部接口
     */
    boolean value() default true;
}

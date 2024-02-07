package io.github.mjyoun.core.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엑셀 시트 정보
 * 
 * @author MJ Youn
 * @since 2024. 02. 07.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelSheet {

    /**
     * 시트 이름 정보. 없을 경우, 기본 이름으로 설정 된다.
     * 
     * @return 시트 이름
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    String value() default "";

}

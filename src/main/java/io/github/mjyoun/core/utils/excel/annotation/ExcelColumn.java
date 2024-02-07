package io.github.mjyoun.core.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엑셀 파일 정보
 * 
 * @author MJ Youn
 * @since 2024. 02. 07.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /**
     * 컬럼 이름 정보. header에 포함될 내용
     * 
     * @return 컬럼 이름
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    String value() default "";

    /**
     * 컬럼 포함 여부. true일 경우, 파일에 포함하지 않는다.
     * 
     * @return 컬럼 포함 여부
     * 
     * @author MJ Youn
     * @since 2024. 02. 07.
     */
    boolean ignore() default false;

}

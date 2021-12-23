package io.github.mjyoun.core.data;

import java.text.MessageFormat;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결과 타입을 반환할 때 사용하는 객체
 * 
 * @author MJ Youn
 * @since 2021. 12. 23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {

    /** 성공시 출력하는 결과 데이터 */
    private T data;
    /** 실패시 출력하는 메시지 정보 */
    private String message;
    /** 실패/성공 여부 */
    private boolean result;

    /**
     * 성공 상태를 생성하는 메소드
     * 
     * @param <T>
     *            성공 데이터 타입
     * @param data
     *            성공한 데이터 값
     * @return 성공 상태
     */
    public static <T> Result<T> ok(T data) {
        return Result.<T>builder() //
                .data(data) //
                .message(null) //
                .result(true) //
                .build();
    }

    /**
     * 실패 상태를 생성하는 메소드
     * 
     * @param <T>
     *            실패 객체 타입
     * @param msg
     *            실패 메시지
     * @param args
     *            실패 메시지에 포함될 argument 목록. <br/>
     *            메시지 상에서 {0}, {1}, ... 위치에 맵핑 됨
     * @return 실패 상태
     */
    public static <T> Result<T> error(@NotNull String msg, Object... args) {
        String message = MessageFormat.format(msg, args);
        return Result.<T>builder() //
                .data(null) //
                .message(message) //
                .result(false) //
                .build();
    }

    /**
     * 실패 상태를 생성하는 메소드
     * 
     * @param <T>
     *            실패 객체 타입
     * @param result
     *            다른 타입의 실패 상태
     * @return 실패 상태
     */
    public static <T> Result<T> error(@NotNull Result<?> result) {
        return Result.<T>builder() //
                .data(null) //
                .message(result.getMessage()) //
                .result(false) //
                .build();
    }

}

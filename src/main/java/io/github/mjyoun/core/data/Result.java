package io.github.mjyoun.core.data;

import java.text.MessageFormat;
import java.util.function.Supplier;

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
     *            실패 메시지에 포함될 argument 목록. <br>
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

    /**
     * 결과 데이터가 1인지 확인하는 함수
     * 
     * @return 결과 데이터가 1이면 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public Result<Boolean> isOne() {
        return this.checkCount(() -> (Integer) this.data == 1);
    }

    /**
     * 결과 데이터가 1이상인지 확인하는 함수
     * 
     * @return 결과 데이터가 1이상이면 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public Result<Boolean> isOneMore() {
        return this.checkCount(() -> (Integer) this.data > 0);
    }

    /**
     * 결과 데이터가 0이상인지 확인하는 함수
     * 
     * @return 결과 데이터가 0이상이면 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public Result<Boolean> isZeroMore() {
        return this.checkCount(() -> (Integer) this.data > -1);
    }

    /**
     * 결과 데이터가 {@link Integer} 타입이고 원하는 값과 동일한지 확인하는 함수
     * 
     * @param count
     *            결과 데이터 비교 값
     * @return 비교 값과 결과 데이터가 같을 경우 true, 아닐 경우 flase
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    public Result<Boolean> isEqualCount(int count) {
        return this.checkCount(() -> (Integer) this.data == count);
    }

    /**
     * 결과 데이터가 {@link Integer} 타입이고 비교 함수의 결과로 결과를 생성하는 함수
     * 
     * @param func
     *            결과 비교 함수
     * @return 비교 함수가 true일 경우 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    private Result<Boolean> checkCount(Supplier<Boolean> func) {
        if (this.data instanceof Integer) {
            return this.isTrue(func);
        }

        throw new NumberFormatException(MessageFormat.format("결과 데이터가 Integer 타입이 아닙니다. [data: {0}]", this.data));
    }

    /**
     * 비교 함수의 결과에 따라서 결과를 생성하는 함수
     * 
     * @param func
     *            비교 함수
     * @return 비교 함수가 true일 경우 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2021. 12. 28.
     */
    private Result<Boolean> isTrue(@NotNull Supplier<Boolean> func) {
        if (!func.get()) {
            return Result.error("실행 결과가 예상 값과 다릅니다. [result: {0}]", this.data);
        } else {
            return Result.ok(true);
        }
    }

}

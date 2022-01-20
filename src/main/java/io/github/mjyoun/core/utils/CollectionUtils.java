package io.github.mjyoun.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Collection 관련된 공통 기능을 관리하기 위한 util
 * 
 * @author MJ Youn
 * @since 2022. 01. 19.
 */
public class CollectionUtils {

    /**
     * primitive int type의 배열을 {@link List}에 {@link Integer} 타입으로 넣어주는 함수
     * 
     * @param target
     *            형 변환한 데이터를 넣기 위한 리스트. null일 경우 신규 생성
     * @param source
     *            primitive type의 배열
     * @return 형 변환된 데이터 리스트
     * 
     * @author MJ Youn
     * @since 2022. 01. 19.
     */
    public static List<Integer> addAllIntPrimitiveType(List<Integer> target, int[] source) {
        if (target == null) {
            target = new ArrayList<>();
        }

        if (source != null && source.length > 0) {
            target.addAll( //
                    Arrays.stream(source) //
                            .boxed() //
                            .collect(Collectors.toList()) //
            );
        }

        return target;
    }

    /**
     * primitive int type의 배열을 {@link List}에 {@link Integer} 타입으로 넣어주는 함수
     * 
     * @param source
     *            primitive type의 배열
     * @return 형 변환된 데이터 리스트
     * 
     * @author MJ Youn
     * @since 2022. 01. 19.
     */
    public static List<Integer> addAllIntPrimitiveType(int[] source) {
        return CollectionUtils.addAllIntPrimitiveType(null, source);
    }

    /**
     * 모든 {@link Collection}이 비어있는지 확인하는 함수 <br>
     * 모든 리스트가 비어 있거나 null이면 true, 아닐 경우 false
     * 
     * @param collections
     *            {@link Collection}
     * @return 모든 리스트가 비어 있거나 null이면 true, 아닐 경우 false
     * 
     * @author MJ Youn
     * @since 2022. 01. 19.
     */
    public static Boolean isAllEmpty(Collection<?>... collections) {
        for (Collection<?> collection : collections) {
            if (collection != null && !collection.isEmpty()) {
                return false;
            }
        }

        return true;
    }

}

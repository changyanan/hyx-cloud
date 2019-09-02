
package com.hyx.core.utils;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.hyx.core.exception.GlobalException;
import com.hyx.core.exception.GlobalExceptionStatus;

/**
 * 断言类
 * 
 * @author leige
 *
 */
public abstract class Assert {

        /**
         * 如果expression是true,就不抛出异常
         * 
         * @param expression
         * @param message
         *            抛出异常的message
         */
        public static void isTrue(boolean expression, String message,Object... args) {
                if (!expression) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        /**
         * 如果expression是true,就不抛出异常
         * 
         * @param expression
         */
        public static void isNull(Object object, String message,Object... args) {
                if (object != null) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        /**
         * 如果object不是null就不抛出异常
         * 
         * @param object
         * @param message
         */
        public static void notNull(Object object, String message,Object... args) {
                if (object == null) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        /**
         * text 是空的就抛出异常
         * 
         * @param text
         * @param message
         */
        public static void hasLength(String text, String message,Object... args) {
                if (!StringUtils.hasLength(text)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void hasText(String text, String message,Object... args) {
                if (!StringUtils.hasText(text)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void doesNotContain(String textToSearch, String substring, String message,Object... args) {
                if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
                                && textToSearch.contains(substring)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void notEmpty(Object[] array, String message,Object... args) {
                if (ObjectUtils.isEmpty(array)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void noNullElements(Object[] array, String message,Object... args) {
                if (array != null) {
                        for (Object element : array) {
                                if (element == null) {
                                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                                }
                        }
                }
        }

        public static void notEmpty(Collection<?> collection, String message,Object... args) {
                if (CollectionUtils.isEmpty(collection)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void notEmpty(Map<?, ?> map, String message,Object... args) {
                if (CollectionUtils.isEmpty(map)) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        public static void state(boolean expression, String message,Object... args) {
                if (!expression) {
                        GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(),message, args);
                }
        }

        /**
         * 如果是null 或者是0就抛出异常
         * 
         * @param num
         * @param string
         */
        public static void notZero(Integer num, String message,Object... args) {
                isTrue(num != null && num != 0, message, args);
        }

        /**
         * 断言 a大于b, a,b任意一个等于null，或者a<=b就抛出异常
         * 
         * @param a
         * @param b
         * @param message
         */
        public static <A extends Comparable<B>, B extends Comparable<A>> void isGreater(A a, B b, String message,Object... args) {
                isTrue(a != null && b != null && a.compareTo(b) > 0, message, args);
        }

        /**
         * 断言 a b相等 , a b不相等就抛出异常
         * 
         * @param a
         * @param b
         * @param message
         */
        public static void isEquals(Object a, Object b, String message,Object... args) {
                isTrue((a == null && b == null) || (a != null && a.equals(b)), message, args);
        }

        /**
         * 断言 a b不相等 , a b相等就抛出异常
         * 
         * @param a
         * @param b
         * @param message
         */
        public static void notEquals(Object a, Object b, String message,Object... args) {
                isTrue((a == null && b != null) || (a != null && !a.equals(b)), message,args);
        }
}

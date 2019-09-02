package org.xuenan.hyx;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 14:57:00
 */
public abstract class StringUtils {
    public static final String BLANK = "";

    public static String joining(String[] format, Object... arguments) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < format.length; i++) {
            if (format[i] != null) {
                sb.append(format[i]);
            }
            if (i < arguments.length && arguments[i] != null) {
                sb.append(arguments[i]);
            }
        }
        return sb.toString();
    }

    public static String joining(String name, Object... arguments) {
        if (arguments.length == 0) {
            return name;
        }
        StringBuilder sb = new StringBuilder(name);
        sb.append("[");
        sb.append(arguments[0]);
        sb.append(",");
        for (int i = 1; i < arguments.length; i++) {
            sb.append(",");
            sb.append(arguments[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static String trimLimitLen(String content,int limit) {
        if(isEmpty(content)) {
            return "";
        }
        content=content.trim();
        if(content.length()<=limit) {
            return content;
        }
        return content.substring(0, limit);
    }

    public static boolean isNotEmpty(String... args) {
        for (String string : args) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String... args) {
        return !isNotEmpty(args);
    }

    /**
     * 转义特殊字符 <转成&lt; >转成&gt; '转成&#039; "转成&quot;
     *
     * @param source
     * @return
     */
    public static String encode(String source) {
        if (isEmpty(source)) {
            return source;
        }
        return source.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&#039;").replaceAll("\"", "&quot;");
    }

    /**
     * 转义特殊字符 &lt;转成< &gt;转成> &#039;转成' &quot;转成"
     *
     * @param source
     * @return
     */
    public static String decode(String source) {
        if (isEmpty(source)) {
            return source;
        }
        return source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#039;", "'").replaceAll("&quot;", "\"");
    }
}

package org.xuenan.hyx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.xuenan.hyx.consts.Const;
import org.xuenan.hyx.model.PlatformEnum;
import org.xuenan.hyx.model.SysUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description 操作上下文 请勿在service层使用
 * @date 2019年09月02日 16:29:00
 */
public class Context {
    private final static ThreadLocal<Context> contextHolder = new ThreadLocal<Context>();

    private static Logger logger = LoggerFactory.getLogger(Context.class);

    private Map<String, Object> contextMap;

    private HttpServletResponse response;

    private HttpServletRequest request;

    /**
     * 当前线程中得消息
     */
    private String message = null;
    /**
     * 处理请求开始的时间,毫秒，
     */
    private Long beginTime = null;

    /**
     * 当前操作的标题
     */
    private String title = null;
    private String clientid = null;
    private String host;
    private String callback = null;
    /**
     * 当前登录的用户，如果存在(线程用户 )
     */
    private SysUser user = null;
    private String hostname = null;
    private String requestUri = null;
    private String userid = null;
    private Boolean ispc = null;
    private Boolean isRest = false;
    private Boolean now = null;

    public static Context getContext() {
        now();
        return contextHolder.get();
    }

    public static boolean now() {
        Context context = contextHolder.get();
        if (context != null)
            return false;
        contextHolder.set(new Context());
        return true;
    }

    public static Context init(HttpServletRequest request, HttpServletResponse response) {
        if (request == null)
            return null;
        Context context = getContext();
        context.request = request;
        context.response = response;
        context.beginTime = System.currentTimeMillis();
        context.host = request.getHeader("Host");
        context.clientid = null;
        context.user = null;
        context.hostname = null;
        context.requestUri = null;
        context.userid = null;
        context.ispc = null;
        context.now = null;
        context.isRest = false;
        return context;
    }

    public static boolean isPc() {
        Context context = getContext();
        if (context.ispc != null) {
            return context.ispc;
        }
        if (context.request == null) {
            return context.ispc = true;
        }
        return true;
    }

    public static boolean isLogin() {
        SysUser user = getUser0();
        return user != null && user.isLogin();
    }

    public static void sendUserId() {
        String userid = Context.getCookie(Const.USERID_COOKIE_NAME);
        if (userid == null) {
            userid = "UID-" + Sequence.generate();
            Context.addCookie(Const.USERID_COOKIE_NAME, userid);
            Context.getContext().userid = userid;
        }
    }

    public static String getCallback() {
        Context context = getContext();
        if (context.callback == null && context.request != null)
            context.callback = context.request.getParameter("jsoncallback");
        return context.callback;
    }

    public static String getHostName() {
        Context context = getContext();
        if (context.hostname != null)
            return context.hostname;
        int endIndex;
        int beginIndex;
        if (context.host != null && 0 < (endIndex = context.host.lastIndexOf('.'))
                && 0 <= (beginIndex = context.host.lastIndexOf('.', endIndex - 1)) && 1 < (endIndex - beginIndex))
            return (context.hostname = context.host.substring(beginIndex + 1, endIndex));
        return (context.hostname = "");
    }

    public static String getHost() {
        return getContext().host;
    }

    public static void isRest(boolean isResEntity) {
        getContext().isRest = isResEntity;
    }

    public static boolean isRest() {
        return getContext().isRest;
    }

    public static boolean isNow() {
        Context context = getContext();
        if (context.now == null) {
            if (Context.get("now") == null)
                context.now = false;
            else
                context.now = true;
        }
        return context.now;
    }

    private final static String error_uri_key = "javax.servlet.error.request_uri";

    public static String getRequestUri() {
        Context context = getContext();
        if (context.requestUri != null)
            return context.requestUri;
        if (context.request == null)
            return context.requestUri = "";
        String error_uri = (String) context.request.getAttribute(error_uri_key);
        if (error_uri != null)
            return (context.requestUri = error_uri);
        else
            return (context.requestUri = context.request.getRequestURI());
    }

    public static void remove() {
        // String title=getTitle();
        // String message=getMessage();
        contextHolder.remove();
    }

    private static SysUser getUser0() {
        Context context = getContext();
        if (context.user != null)
            return context.user;
        if (context.request == null)
            return context.user = null;
        return ContextUtils.initLoginInfo();
    }

    /**
     * 获取当前用户，不校验用户是否登录
     *
     * @return
     */
    public static SysUser getUser() {
        return getUser0();
    }

    /**
     * 获取当前用户，并且判断是否登录
     *
     * @return
     */
    public static SysUser getLoginUser() {
        if (!isLogin())
            GlobalException.exception(GlobalExceptionStatus.UNAUTHORIZED);
        return getUser0();
    }

    public static void setUser(SysUser user) {
        getContext().user = user;
    }

    public static long getBeginTime() {
        Context context = getContext();
        if (context.beginTime == null)
            return System.currentTimeMillis();
        return context.beginTime;
    }

    public static String getMessage() {
        return getContext().message;
    }

    public static void setMessage(String message) {
        getContext().message = message;
    }

    public static String getTitle() {
        return getContext().title;
    }

    public static void setTitle(String titleformat, Object... arg1) {
        getContext().title = String.format(titleformat, arg1);
    }

    public static void setRequest(HttpServletRequest request) {
        getContext().request = request;
    }

    public static void setResponse(HttpServletResponse response) {
        getContext().response = response;
    }

    public static HttpServletRequest getRequest() {
        return getContext().request;
    }

    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null)
            return null;
        for (int i = 0; i < 3; i++)
            try {
                return request.getSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;

    }

    public static HttpServletResponse getResponse() {
        return getContext().response;
    }

    /**
     * 设置值全部设置的request
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        Context context = getContext();
        if (context.request != null)
            context.request.setAttribute(key, value);
        else {
            if (context.contextMap == null)
                context.contextMap = new LinkedHashMap<>();
            context.contextMap.put(key, value);
        }
    }

    /**
     * 首先从request中取，取不到从请求参数中取，继续道请求头中取，继续到会话中取，继续到ServletContext中取,如果依然取不到，就返回null
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        Object value = getForServer(key);
        if (value != null)
            return value;
        return getForRequest(key);
    }

    /**
     * 从
     * cache,request.getAttribute,servletContext.getAttribute,session.getAttribute
     * 中获取，按顺序
     *
     * @param key
     * @return
     */
    public static Object getForServer(String key) {
        Context context = getContext();
        Map<String, Object> map = context.contextMap;
        if (map != null && map.containsKey(key))
            return map.get(key);
        if (context.request == null)
            return null;
        HttpServletRequest request = context.request;
        Object value = null;
        if ((value = request.getAttribute(key)) != null)
            return value;
        ServletContext servletContext = request.getServletContext();
        if ((value = servletContext.getAttribute(key)) != null)
            return value;
        HttpSession session = getSession();
        if (session != null)
            return session.getAttribute(key);
        return null;
    }

    /**
     * 从Parameter,Header,InitParameter,Cookie中获取，按顺序
     *
     * @param key
     * @return
     */
    public static String getForRequest(String key) {
        HttpServletRequest request = getRequest();
        if (request == null)
            return null;
        String value = null;
        if ((value = request.getParameter(key)) != null)
            return value;
        if ((value = request.getHeader(key)) != null)
            return value;
        ServletContext servletContext = request.getServletContext();
        if ((value = servletContext.getInitParameter(key)) != null)
            return value;
        return getCookie(key);
    }

    public static String getCookie(String name) {
        if (StringUtils.isEmpty(name)) {
            logger.debug("Cookie Name 为空 ：CookieName={}", name);
            return null;
        }
        HttpServletRequest request = getRequest();
        if (request == null)
            return null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.debug("没有取到 Cookie ：CookieName={}", name);
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName()))
                try {
                    String cookie_value = cookie.getValue();
                    cookie_value = URLDecoder.decode(cookie_value, Const.DEFAULT_CHARSET.name());
                    logger.debug(" Cookie中取到值 ：CookieName={},CookieValue={}", name, cookie_value);
                    return cookie_value;
                } catch (UnsupportedEncodingException e) {
                    logger.warn("读取cookie name={},出现异常", name, e);
                }
        }

        logger.debug(" Cookie中没有取到值 ：CookieName={}", name);
        return null;
    }

    public static void addCookie(String name, String value) {
        Context.addCookie(name, value, null, null, null, null);
    }

    public static void addCookie(String name, String value, String domain) {
        Context.addCookie(name, value, domain, null, null, null);
    }

    public static void addCookie(String name, String value, Integer maxage) {
        Context.addCookie(name, value, null, maxage, null, null);
    }

    public static void addCookie(String name, String value, String domain, Integer maxage) {
        Context.addCookie(name, value, domain, maxage, null, null);
    }

    public static void addCookie(String name, String value, String domain, Integer maxage, Boolean httpOnly,
                                 Integer version) {
        HttpServletResponse response = getResponse();
        if (response == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
            return;
        }
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, Const.DEFAULT_CHARSET.name()));
            if (domain != null) {
                cookie.setDomain(domain);
            }
            if (maxage == null) {
                maxage = Integer.MAX_VALUE;
            }
            cookie.setMaxAge(maxage);
            if (httpOnly != null) {
                cookie.setHttpOnly(httpOnly);
            }
            if (version != null) {
                cookie.setVersion(version);
            }
            cookie.setPath("/");
            response.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            logger.info("下发Cookie {}失败", name, e);
        }
    }

    public static void delCookie(String name) {
        delCookie(name, null);
    }

    public static void delCookie(String name, String domain) {
        if (name == null) {
            return;
        }
        HttpServletResponse response = getResponse();
        if (response == null) {
            logger.info("response对象为空....");
            return;
        }
        logger.debug("即将从cookie删除{},", name);
        Cookie delcookie = new Cookie(name, "");
        delcookie.setPath("/");
        if (domain != null) {
            delcookie.setDomain(domain);
        }
        delcookie.setMaxAge(0);
        response.addCookie(delcookie);
    }

    /***
     * 获取客户端IP地址;这里通过了Nginx获取;X-Real-IP,
     *
     * @return
     */
    private final static String[] keys = new String[] { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
            "WL-Proxy-Client-IP" };

    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        for (String key : keys) {
            String ip = request.getHeader(key);
            if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取请求的客户端ip链路
     *
     * @return
     */
    public static String getClientIPLink() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(keys[0]);
    }

    /**
     * 客户端转发至redirecturi
     */
    public static void sendRedirect(String redirecturi) {
        logger.info("请求转发至" + redirecturi);
        HttpServletResponse response = getResponse();
        if (response == null)
            return;
        try {
            response.sendRedirect(redirecturi);
        } catch (IOException e) {
            logger.warn("请求转发至" + redirecturi + "出现异常", e);
        }
    }

    /**
     * 服务器端转发至
     *
     * @param forwarduri
     */
    public static void forward(String forwarduri) {
        logger.info("请求转发至" + forwarduri);
        HttpServletRequest request = getRequest();
        if (request == null)
            return;
        try {
            request.getRequestDispatcher(forwarduri).forward(request, getResponse());
        } catch (IOException | ServletException e) {
            logger.warn("请求转发至" + forwarduri + "出现异常", e);
        }
    }

    public static void download(File file) throws IOException {
        Assert.notNull(file, "文件不存在");
        Assert.isTrue(file.exists(), "文件不存在");
        String fileName = file.getName();
        long length = file.length();
        InputStream inputStream = new FileInputStream(file);
        try {
            download(fileName, length, inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static void download(String fileName, long length, InputStream inputStream) throws IOException {
        HttpServletResponse response = getResponse();
        response.reset();
        response.setContentType("application/octet-stream");
        if (fileName != null) {
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
        }
        response.addHeader("Content-Length", String.valueOf(length));
        ServletOutputStream out = response.getOutputStream();
        try {
            StreamUtils.copy(inputStream, out);
        } finally {
            out.flush();
            out.close();
        }
    }

    public static String getUserId() {
        if (isLogin())
            return getUser0().getUserId();
        String userid = getContext().userid;
        if (userid != null)
            return userid;
        return getCookie(Const.USERID_COOKIE_NAME);
    }

    public static String getClientid() {
        Context context = getContext();
        if (context.clientid == null)
            context.clientid = String.valueOf(get("client"));
        return context.clientid;
    }

    public static void setClientId(String clientId) {
        getContext().clientid = clientId;
    }

    public static String getScheme() {
        return getRequest().getScheme();
    }

    @Override
    public String toString() {
        String referer = getForRequest("Referer");
        String userAgent = getForRequest("User-Agent");
        String clientid = getClientid();
        String clientIP = getClientIP();
        String deviceid = getDeviceid();
        Integer versionCode = getCurversionCode();
        String userId = getUserId();
        PlatformEnum platform = getCurplatform();
        String clientIPLink = getClientIPLink();
        StringBuffer message = new StringBuffer(" [[请求上下文信息 ]]  ");
        message.append("\n    >>>> 请求地址:[").append(getHost()).append(getRequestUri()).append("]");
        if (referer != null) {
            message.append("\n      >>>>>>  请求来源: [").append(referer).append("]");
        }
        if (userAgent != null) {
            message.append("\n      >>>>>>  客户端信息:[").append(userAgent).append("]");
        }
        if (clientid != null) {
            message.append("\n      >>>>>>  客户端编号:[").append(clientid).append("]");
        }
        if (clientIP != null) {
            message.append("\n      >>>>>>  客户端IP:[").append(clientIP).append("]");
        }
        if (deviceid != null) {
            message.append("\n      >>>>>>  deviceid:[").append(deviceid).append("]");
        }
        if (versionCode != null) {
            message.append("\n      >>>>>>  versionCode:[").append(versionCode).append("]");
        }
        if (userId != null) {
            message.append("\n      >>>>>>  userId:[").append(userId).append("]");
        }
        if (platform != null) {
            message.append("\n      >>>>>>  platform:[").append(platform).append("]");
        }
        if (clientIPLink != null) {
            message.append("\n      >>>>>>  客户端ip链路:[").append(clientIPLink).append("]");
        }
        if (this.message != null) {
            message.append("\n      >>>>>>  上下文描述信息:[").append(this.message).append("]");
        }
        return message.toString();
    }

    private final static String sign_in_para = "sign";
    private final static String timestamp_in_para = "timestamp";
    private final static String deviceid_in_para = "deviceId";
    private final static String curplatform_in_para = "curPlatform";
    private final static String curversioncode_in_para = "curVersionCode";
    private final static String cur_app_channel_id = "appChannelId";

    public static String getSign() {
        return getForRequest(sign_in_para);
    }

    public static Long getTimestamp() {
        String timestamp = getForRequest(timestamp_in_para);
        if (StringUtils.isEmpty(timestamp)) {
            return null;
        }
        try {
            return (long) Double.parseDouble(timestamp);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDeviceid() {
        return getForRequest(deviceid_in_para);
    }

    public static PlatformEnum getCurplatform() {
        String curPlatform = getForRequest(curplatform_in_para);
        if (StringUtils.isEmpty(curPlatform)) {
            return null;
        }
        try {
            return PlatformEnum.valueOfName(curPlatform);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getCurversionCode() {
        String curVersionCode = getForRequest(curversioncode_in_para);
        if (StringUtils.isEmpty(curVersionCode)) {
            return null;
        }
        try {
            return (int) Double.parseDouble(curVersionCode);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurAppChannelId() {
        return getForRequest(cur_app_channel_id);
    }
}

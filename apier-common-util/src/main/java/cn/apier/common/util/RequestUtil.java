package cn.apier.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.MessageFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * HttpServletRequest实用类。<bt/>
 * 
 * @author huangyongqiang
 * @since 2011-06-23
 * 
 */
public final class RequestUtil
{

	public static final String AJAX_REQUEST_HEADER_NAME = "X-Requested-With";

	public static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";

	/**
	 * 获取MAC地址的数据包
	 */
	private static final byte[] BQ_CMD = new byte[50];

	/**
	 * 对方的端口
	 */
	private static final int REMOTE_PORT = 137;

	static {
		BQ_CMD[0] = 0x00;
		BQ_CMD[1] = 0x00;
		BQ_CMD[2] = 0x00;
		BQ_CMD[3] = 0x10;
		BQ_CMD[4] = 0x00;
		BQ_CMD[5] = 0x01;
		BQ_CMD[6] = 0x00;
		BQ_CMD[7] = 0x00;
		BQ_CMD[8] = 0x00;
		BQ_CMD[9] = 0x00;
		BQ_CMD[10] = 0x00;
		BQ_CMD[11] = 0x00;
		BQ_CMD[12] = 0x20;
		BQ_CMD[13] = 0x43;
		BQ_CMD[14] = 0x4B;

		for (int i = 15; i < 45; i++) {
			BQ_CMD[i] = 0x41;
		}

		BQ_CMD[45] = 0x00;
		BQ_CMD[46] = 0x00;
		BQ_CMD[47] = 0x21;
		BQ_CMD[48] = 0x00;
		BQ_CMD[49] = 0x01;
	}
	private static final Logger LOG = LoggerFactory
			.getLogger(RequestUtil.class);

	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static String getQueryParam(HttpServletRequest request, String name,
			String encoding) {
		String s = request.getQueryString();
		if (StringUtils.isBlank(s)) {
			return null;
		}
		try {
			s = URLDecoder.decode(s, encoding);
		} catch (UnsupportedEncodingException e) {
			LOG.error("encoding {} not support.", encoding);
		}
		if (StringUtils.isBlank(s)) {
			return null;
		}
		String[] values = parseQueryString(s).get(name);
		if (values != null && values.length > 0) {
			return values[values.length - 1];
		} else {
			return null;
		}
	}

	/**
	 * 
	 * Parses a query string passed from the client to the server and builds a
	 * <code>HashTable</code> object with key-value pairs. The query string
	 * should be in the form of a string packaged by the GET or POST method,
	 * that is, it should have key-value pairs in the form <i>key=value</i>,
	 * with each pair separated from the next by a &amp; character.
	 * 
	 * <p>
	 * A key can appear more than once in the query string with different
	 * values. However, the key appears only once in the hashtable, with its
	 * value being an array of strings containing the multiple values sent by
	 * the query string.
	 * 
	 * <p>
	 * The keys and values in the hashtable are stored in their decoded form, so
	 * any + characters are converted to spaces, and characters sent in
	 * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
	 * 
	 * @param s
	 *            a string containing the query to be parsed
	 * 
	 * @return a <code>HashTable</code> object built from the parsed key-value
	 *         pairs
	 * 
	 * @exception IllegalArgumentException
	 *                if the query string is invalid
	 * 
	 */
	public static Map<String, String[]> parseQueryString(String s) {
		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = (String) st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (ht.containsKey(key)) {
				String oldVals[] = (String[]) ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++)
					valArray[i] = oldVals[i];
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}
		return ht;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getRequestMap(HttpServletRequest request,
			String prefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				request.getParameterValues(name);
				map.put(name.substring(prefix.length()),
						StringUtils.join(request.getParameterValues(name), ','));
			}
		}
		return map;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		String ip = request.getHeader("X-Real-IP");

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			/* 多次反向代理后会有多个IP值，第一个为真实IP。 */
			int index = ip.indexOf(',');
			if (-1 != index) {
				String[] ipArr = ip.split(",");
				int size = ipArr.length;
				for (int i = 0; i < size; i++) {
					ip = ipArr[i];
					if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
						break;
					}
				}
			}
		}

		return ip;
	}

	/**
	 * 取得MAC地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getMACAddress(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		String ip = getIpAddr(request);
		return getMACAddressByCmd(ip);
	}

	/**
	 * 通过IP地址用命令的方式取得MAC地址
	 * 
	 * @param ip
	 *            对方的IP地址
	 * @return
	 */
	public static String getMACAddressByCmd(String ip) {
		if (StringUtils.isBlank(ip)) {
			return "";
		}
		String macAddress = "";
		Process p = null;
		String osName = System.getProperty("os.name").toUpperCase();
		// System.out.println(">>>>>>>>>>>>>osName   "+osName+"  >>>>>>>>>>>>>>>>");
		String fileSeparator = System.getProperty("file.separator")
				.toUpperCase();
		// System.out.println(">>>>>>>>>>>>>fileSeparator   "+fileSeparator+"  >>>>>>>>>>>>>>>>");
		try {
			if (osName.indexOf("WINDOWS") != -1
					&& fileSeparator.startsWith("\\")) {
				p = Runtime.getRuntime().exec(
						MessageFormat.format("nbtstat -A {0}", ip));
			} else if (osName.indexOf("LINUX") != -1
					&& fileSeparator.startsWith("/")) {
				p = Runtime.getRuntime().exec(
						MessageFormat.format("nmblookup -A {0}", ip));
			} else {
				LOG.warn("位置的操作系统类型 {}", osName);
				return "";
			}
		} catch (IOException e) {
			LOG.error("取得IP地址 {}的MAC地址出错", ip);
			return "";
		}
		if (null == p) {
			return "";
		}
		LineNumberReader input = new LineNumberReader(new InputStreamReader(
				p.getInputStream()));
		String str = "";
		try {
			str = input.readLine();
		} catch (IOException e) {
			LOG.error("读取通过IP地址 {}执行nbtstat取得MAC地址出错", ip);
			return "";
		}
		do {
			// System.out.println(">>>>>>>>>>>>>  "+str+"  >>>>>>>>>>>>>>>>");
			if (StringUtils.isNotBlank(str)) {
				if (str.indexOf("MAC Address") > -1) {
					macAddress = str.substring(str.indexOf("MAC Address") + 14,
							str.length());
					return macAddress;
				}
			}
			try {
				str = input.readLine();
			} catch (IOException e) {
				LOG.error("读取通过IP地址 {}执行nbtstat取得MAC地址出错", ip);
				continue;
			}
		} while (true);
	}

	/**
	 * 通过IP地址用数据包的方式取得MAC地址
	 * 
	 * @param ip
	 *            对方的IP地址
	 * @return
	 */
	public static String getMACAddressByDatagramPacket(String ip) {
		if (StringUtils.isBlank(ip)) {
			return "";
		}
		DatagramPacket dpSend = null;
		DatagramSocket ds = null;
		DatagramPacket dpReceive = null;
		try {
			dpSend = new DatagramPacket(BQ_CMD, BQ_CMD.length,
					InetAddress.getByName(ip), REMOTE_PORT);
			ds = new DatagramSocket();
			ds.send(dpSend);
			byte[] buffer = new byte[1024];
			dpReceive = new DatagramPacket(buffer, buffer.length);

		} catch (UnknownHostException e) {
			LOG.error("读取通过IP地址 {} 和端口 {} 连接出错", ip, REMOTE_PORT);
			return "";
		} catch (SocketException e) {
			LOG.error("构造数据包Socket出错", e);
			return "";
		} catch (IOException e) {
			LOG.error("发送 接收数据包出错", e);
			return "";
		}
		byte[] recevieData = dpReceive.getData();
		int i = recevieData[56] * 18 + 56;
		String macAddr = "";
		StringBuffer sb = new StringBuffer(17);
		for (int j = 1; j < 7; j++) {
			macAddr = Integer.toHexString(0xFF & recevieData[i + j]);
			if (macAddr.length() < 2) {
				sb.append(0);
			}
			sb.append(macAddr.toUpperCase());
			if (j < 6) {
				sb.append('-');
			}
		}
		return sb.toString();
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		StringBuffer sb = request.getRequestURL();
		if (request.getQueryString() != null) {
			sb.append("?").append(request.getQueryString());
		}
		return sb.toString();
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * 
	 * @param lastModified
	 *            内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request,
			HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * 
	 * @param etag
	 *            内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
			HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(
						headerValue, ",");
				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response,
			String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(),
					"ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters.
	 * 
	 * 返回的结果Parameter名已去除前缀.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParametersStartingWith(
			HttpServletRequest request, final String paramPrefix) {
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String prefix = paramPrefix;
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {// NOSONAR
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic(String userName, String password) {
		String encode = String.format("%s:%s", userName, password);
		return String.format("Basic %s", Base64.getEncoder().encode(encode.getBytes()));
	}

	/**
	 * 获得当前请求的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpOfRequest(HttpServletRequest request) {
		String ret = "";
		if (null != request) {
			ret = RequestUtil.getIpAddr(request);
		}
		return ret;
	}

	/**
	 * 获得处理当前请求的服务器端口。<br/>
	 * 
	 * @param request
	 * @author luke huang
	 * @since 2.1.8
	 * @return
	 */
	public static Integer getServerPortOfRequest(HttpServletRequest request) {
		if (null == request) {
			return null;
		}
		return request.getLocalPort();
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getPathOfRequest(HttpServletRequest request) {
		String ret = "";
		if (null != request) {
			ret = RequestUtil.getLocation(request);
		}
		return ret;
	}

	/**
	 * 设置客户端缓存过期时间 Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response,
			long expiresSeconds) {
		// Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis()
				+ expiresSeconds * 1000);
		// Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age="
				+ expiresSeconds);
	}

	/**
	 * 设置客户端无缓存Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 0);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache");
	}

	/**
	 * 设置LastModified Header.
	 */
	protected static void setLastModifiedHeader(HttpServletResponse response,
			long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			String purpose, String domainPattern, Integer expiry, String uri,
			Boolean secure, Integer version) {
		String _name = trimToEmpty(name);
		if (Objects.isNull(response) || StringUtils.isBlank(_name)) {
			return;
		}

		Cookie cookie = new Cookie(_name, value);
		if (StringUtils.isNotBlank(purpose)) {
			cookie.setComment(purpose);
		}
		if (StringUtils.isNotBlank(domainPattern)) {
			cookie.setDomain(domainPattern);
		}
		if (null != expiry && 0 != expiry) {
			/* 0表示删除cookie */
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(uri)) {
			cookie.setPath(uri);
		}
		cookie.setSecure(null != secure ? secure : Boolean.FALSE);
		if (null != version) {
			cookie.setVersion(version);
		}
		response.addCookie(cookie);
	}

	/**
	 * 
	 * <b>@Title:</b>modifyCookieValue<br/>
	 * <b>@Description:</b> 修改cookie的值。 <br/>
	 *
	 * <b>@author<b> huangyongqiang<br/>
	 * <b>@version</b> 1.1.3<br/>
	 * <b>@since</b> 1.1.3<br/>
	 * 
	 * @param request
	 * @param response
	 * @param name
	 *            cookie名称
	 * @param newValue
	 *            新的值
	 *
	 */
	public static void modifyCookieValue(HttpServletRequest request,
			HttpServletResponse response, String name, String newValue) {
		String _name = trimToEmpty(name);
		if (Objects.isNull(response) || StringUtils.isBlank(_name)) {
			return;
		}

		Cookie[] cookies = request.getCookies();
		if ((cookies.length==0)) {
			return;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (Objects.isNull(cookie)) {
				continue;
			}
			if (_name.equals(cookie.getName())) {
				cookie.setValue(newValue);
				response.addCookie(cookie);
				return;
			}
		}
	}

	/**
	 * 
	 * <b>@Title:</b>modifyCookie<br/>
	 * <b>@Description:</b> 修改cookie。 <br/>
	 *
	 * <b>@author<b> huangyongqiang<br/>
	 * <b>@version</b> 1.1.3<br/>
	 * <b>@since</b> 1.1.3<br/>
	 * 
	 * @param request
	 * @param response
	 * @param name
	 *            cookie名称
	 * @param newValue
	 *            新的值
	 * @param purpose
	 *            用途
	 * @param domainPattern
	 *            域表达式
	 * @param expiry
	 *            过期时间
	 * @param uri
	 *            访问路径
	 * @param secure
	 *            是否安全
	 * @param version
	 *            版本
	 *
	 */
	public static void modifyCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String newValue,
			String purpose, String domainPattern, Integer expiry, String uri,
			Boolean secure, Integer version) {
		String _name = trimToEmpty(name);
		if (Objects.isNull(response) || StringUtils.isBlank(_name)) {
			return;
		}

		Cookie[] cookies = request.getCookies();
		if ((cookies.length==0)) {
			return;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (Objects.isNull(cookie)) {
				continue;
			}
			if (_name.equals(cookie.getName())) {
				cookie.setValue(newValue);
				if (StringUtils.isNotBlank(purpose)) {
					cookie.setComment(purpose);
				}
				if (StringUtils.isNotBlank(domainPattern)) {
					cookie.setDomain(domainPattern);
				}
				if (null != expiry && 0 != expiry) {
					/* 0表示删除cookie */
					cookie.setMaxAge(expiry);
				}
				if (StringUtils.isNotBlank(uri)) {
					cookie.setPath(uri);
				}
				cookie.setSecure(null != secure ? secure : Boolean.FALSE);
				if (null != version) {
					cookie.setVersion(version);
				}
				response.addCookie(cookie);
				return;
			}
		}
	}

	/**
	 * 
	 * <b>@Title:</b>deleteCookie<br/>
	 * <b>@Description:</b> 删除cookie。 <br/>
	 *
	 * <b>@author<b> huangyongqiang<br/>
	 * <b>@version</b> 1.1.3<br/>
	 * <b>@since</b> 1.1.3<br/>
	 * 
	 * @param request
	 * @param response
	 * @param name
	 *            cookie名称
	 *
	 */
	public static void deleteCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		String _name = trimToEmpty(name);
		if (Objects.isNull(response) || Objects.isNull(request) || StringUtils.isBlank(_name)) {
			return;
		}

		Cookie[] cookies = request.getCookies();
		if ((cookies.length==0)) {
			return;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (Objects.isNull(cookie)) {
				continue;
			}
			if (_name.equals(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setValue(null);
				response.addCookie(cookie);
				return;
			}
		}
	}

	public static String getCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String defaultValue) {
		String _name = trimToEmpty(name);
		if (Objects.isNull(response) || Objects.isNull(request) || StringUtils.isBlank(_name)) {
			return defaultValue;
		}

		Cookie[] cookies = request.getCookies();
		if ((cookies.length==0)) {
			return defaultValue;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (Objects.isNull(cookie)) {
				continue;
			}
			if (_name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return defaultValue;
	}

	public static String getUserAgent(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isBlank(userAgent)) {
			userAgent = request.getHeader("S-User-Agent");
		}
		return userAgent;
	}

	/**
	 * 
	 * <b>@Title:</b>isAjaxRequest<br/>
	 * <b>@Description:</b> 判断是否是<code>AJAX</code>请求。 <br/>
	 *
	 * <b>@author<b> huangyongqiang<br/>
	 * <b>@version</b> 1.1.1<br/>
	 * <b>@since</b> 1.1.1<br/>
	 * 
	 * @param request
	 * @return
	 *
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return (null != request && RequestUtil.AJAX_REQUEST_HEADER_VALUE
				.equals(request.getHeader(RequestUtil.AJAX_REQUEST_HEADER_NAME)));
	}

	private RequestUtil() {

	}
}

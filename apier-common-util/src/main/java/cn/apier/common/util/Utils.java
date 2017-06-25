package cn.apier.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


abstract public class Utils {
	
	final private static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	public static String buildUrl(HttpServletRequest request) {
		LOGGER.debug("build url for request [{}]", request);
		StringBuffer url = new StringBuffer(request.getRequestURL());
		Map<String, String[]> parStringMap = request.getParameterMap();
		if (Objects.nonNull(parStringMap) && !parStringMap.isEmpty()) {
			url.append("?");
			AtomicBoolean first = new AtomicBoolean(true);
			parStringMap.forEach((key, value) -> {
				if (!first.get()) {
					url.append("&");
					first.set(false);
				}
				Arrays.asList(value).forEach(v -> {
					url.append(key + "=" + v);
				});
			});
		}
		LOGGER.debug("url for request: [{}]", url);
		return url.toString();
	}

	public static String md5(String input) {
		Objects.requireNonNull(input);
		LOGGER.debug("string to MD5:{} ", input);
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte b[] = md.digest();
			StringBuffer buf = new StringBuffer();
			for (int i = 1; i < b.length; i++) {
				int c = b[i] >>> 4 & 0xf;
				buf.append(Integer.toHexString(c));
				c = b[i] & 0xf;
				buf.append(Integer.toHexString(c));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("No MD5 Algorithm.");
		}
		LOGGER.debug("MD5 to input [{}] is [{}]", input, result);
		return result;
	}

	private static final String NUMBERCHAR = "0123456789";

	public static String generateMixNumber(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
		}
		return sb.toString();
	}

	public static boolean orderPermit(String deliveryDate, String deadline) {
		Date currentDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentStr = dateFormat.format(currentDate);
		LOGGER.debug("current time string:[{}]", currentDate);
		String deadlineDate = deliveryDate + " " + deadline;

		boolean result = deadlineDate.compareTo(currentStr) > 0;
		LOGGER.debug("result:[{}]", result);
		return true;
	}
}
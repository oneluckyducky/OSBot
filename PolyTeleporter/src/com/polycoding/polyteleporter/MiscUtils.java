package com.polycoding.polyteleporter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MiscUtils {

	public static String getUpdatedVersion() {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		String content = "";

		try {
			url = new URL("http://polycoding.com/osb/powertrainer.v.txt");
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				content += line;
			}
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String insertCommasToNumber(String number) {
		return insertCommasToNumber(number.substring(0, number.length() - 3)) + ","
				+ number.substring(number.length() - 3, number.length());
	}

	public static String getRuntimeFormat(long totalPlayTime) {
		int sec = (int) (totalPlayTime / 1000L);
		int h = sec / 3600;
		int m = sec / 60 % 60;
		int s = sec % 60;
		return (h < 10 ? "0" + h : Integer.valueOf(h)) + ":"
				+ (m < 10 ? "0" + m : Integer.valueOf(m)) + ":"
				+ (s < 10 ? "0" + s : Integer.valueOf(s));
	}

	public static String timeTolevel(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
		if (days == 0L) {
			res = hours + ":" + minutes + ":" + seconds;
		} else {
			res = days + ":" + hours + ":" + minutes + ":" + seconds;
		}
		return res;
	}
}

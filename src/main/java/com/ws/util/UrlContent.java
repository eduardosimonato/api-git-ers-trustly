package com.ws.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlContent {

	public static CharSequence getURLContent(URL url) throws IOException {

		URLConnection conn = url.openConnection();
		String encoding = conn.getContentEncoding();
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		StringBuilder sb = new StringBuilder(100000);
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			br.close();
		}
		return sb;
	}

}

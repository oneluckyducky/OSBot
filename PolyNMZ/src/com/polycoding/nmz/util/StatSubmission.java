package com.polycoding.nmz.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import com.polycoding.nmz.PolyNMZ;

public class StatSubmission {

	private final String putUrlUnformatted = "http://polycoding.com/osb/sigs/polynmz/?put&user=%s&experience=%s&runtime=%s&points=%s&dreams=%s";

	public String putUrl = "";

	PolyNMZ s = null;

	public StatSubmission(PolyNMZ s) {
		this.s = s;
	}

	public void sendStats(String username, String runtime, String experience, String dreams, String points) {
		if (!putUrl.isEmpty())
			putUrl = "";
		putUrl = String.format(putUrlUnformatted, username, experience, runtime, points, dreams);
		try {
			final URL url = new URL(putUrl);
			final InputStream con = url.openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(con);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			String newString = null;

			//JOptionPane.showMessageDialog(null, url.toString());
			while ((line = bufferedReader.readLine()) != null) {
				newString += line;
			}
			// JOptionPane.showMessageDialog(null, newString);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

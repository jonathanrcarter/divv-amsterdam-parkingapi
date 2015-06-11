package com.glimworm.opendata.divvamsterdamapi.planning.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

	public static boolean writeFile(String F, String S) {
		return writeFile(F,S,false); // not append
	}
	public static boolean writeFile(String F, String S, boolean APPEND) {

		File f = new File(F);

		try {
			FileWriter fw = new FileWriter(f.getCanonicalFile(), APPEND);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(S);
			out.close();
			fw.close();

		} catch (Exception E) {
			System.out.println(" gwUtils : writeFile : " + E.getMessage());
			return false;
		}
		return true;

	}

	public static String readFile(String F) {

		File f = new File(F);
		System.out.println(" trying " + F);
		if (!f.exists()) return null;
		System.out.println(" Exists " + F);

		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(f.getCanonicalFile());
			BufferedReader in = new BufferedReader(fr);

			String line = null;

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			in.close();
			fr.close();

		} catch (Exception E) {
			System.out.println(" gwExtras : readFile : " + E.getMessage());
			return null;
		}

		return sb.toString();

	}

	public static boolean delFile(String F) {

		File f = new File(F);
		if (!f.exists()) return true;

		try {
			f.delete();
		} catch (Exception E) {
			System.out.println(" gwExtras : delFile : " + E.getMessage());
			return false;
		}
		return true;
	}

	public static String readFileCR(String F) {

		File f = new File(F);
//		try {
//			System.out.println(" trying " + f.getCanonicalPath());
//			System.out.println(" trying " + f.getAbsolutePath());
//			System.out.println(" trying " + f.getCanonicalPath());
//		} catch (Exception E) {
//			E.printStackTrace();
//		}
		if (!f.exists()) return null;
//		System.out.println(" Exists " + f);

		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(f.getCanonicalFile());
			BufferedReader in = new BufferedReader(fr);

			String line = null;

			while ((line = in.readLine()) != null) {
				sb.append(line+"\n");
			}

			in.close();
			fr.close();

		} catch (Exception E) {
			System.out.println(" gwExtras : readFile : " + E.getMessage());
			return null;
		}

		return sb.toString();

	}

	public static String readFileCR(String F, String TAG) {

		File f = new File(F);
//		try {
//			System.out.println(" trying " + f.getCanonicalPath());
//			System.out.println(" trying " + f.getAbsolutePath());
//			System.out.println(" trying " + f.getCanonicalPath());
//		} catch (Exception E) {
//			E.printStackTrace();
//		}
		if (!f.exists()) return null;
//		System.out.println(" Exists " + f);

		StringBuffer sb = new StringBuffer();
		boolean found = false;

		try {
			FileReader fr = new FileReader(f.getCanonicalFile());
			BufferedReader in = new BufferedReader(fr);

			String line = null;

			wloop:
			while ((line = in.readLine()) != null) {
				if (line.equals("::["+TAG+"]")) {
					found = true;
					continue wloop;
				}
				if (!found) continue wloop;
				if (found && line.startsWith("::[")) break wloop;
				sb.append(line+"\n");
			}

			in.close();
			fr.close();

		} catch (Exception E) {
			System.out.println(" gwExtras : readFile : " + E.getMessage());
			return null;
		}

		return sb.toString();

	}


	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1,3));
		}
		return sb.toString();
	}

	public static String md5 (String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex (md.digest(message.getBytes("CP1252")));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

}

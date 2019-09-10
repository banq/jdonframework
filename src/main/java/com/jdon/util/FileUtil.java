/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileUtil {

	private static String ENCODING = "UTF-8";

	/**
	 * write the content to event file;
	 * 
	 * @param output
	 * @param content
	 * @throws Exception
	 */
	public static void createFile(String output, String content) throws Exception {
		OutputStreamWriter fw = null;
		PrintWriter out = null;
		try {
			if (ENCODING == null)
				ENCODING = PropsUtil.ENCODING;

			fw = new OutputStreamWriter(new FileOutputStream(output), ENCODING);
			out = new PrintWriter(fw);
			out.print(content);
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			if (out != null)
				out.close();
			if (fw != null)
				fw.close();
		}

	}

	/**
	 * read the content from event file;
	 * 
	 * @param output
	 * @param content
	 * @throws Exception
	 */
	public static String readFile(String input) throws Exception {
		char[] buffer = new char[4096];
		int len = 0;
		StringBuilder content = new StringBuilder(4096);

		if (ENCODING == null)
			ENCODING = PropsUtil.ENCODING;
		InputStreamReader fr = null;
		BufferedReader br = null;
		try {
			fr = new InputStreamReader(new FileInputStream(input), ENCODING);
			br = new BufferedReader(fr);
			while ((len = br.read(buffer)) > -1) {
				content.append(buffer, 0, len);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (br != null)
				br.close();
			if (fr != null)
				fr.close();
		}
		return content.toString();
	}

	/**
	 * This class moves an input file to output file
	 * 
	 * @param String
	 *            input file to move from
	 * @param String
	 *            output file
	 * 
	 */
	public static void move(String input, String output) throws Exception {
		File inputFile = new File(input);
		File outputFile = new File(output);
		try {
			inputFile.renameTo(outputFile);
		} catch (Exception ex) {
			throw new Exception("Can not mv" + input + " to " + output + ex.getMessage());
		}
	}

	/**
	 * This class copies an input file to output file
	 * 
	 * @param String
	 *            input file to copy from
	 * @param String
	 *            output file
	 */
	public static boolean copy(String input, String output) throws Exception {
		int BUFSIZE = 65536;
		FileInputStream fis = new FileInputStream(input);
		FileOutputStream fos = new FileOutputStream(output);

		try {
			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1) {
				fos.write(buf, 0, s);
			}

		} catch (Exception ex) {
			throw new Exception("makehome" + ex.getMessage());
		} finally {
			fis.close();
			fos.close();
		}
		return true;
	}

	/**
	 * create event directory
	 * 
	 * @param home
	 * @throws Exception
	 */
	public static void makehome(String home) throws Exception {
		File homedir = new File(home);
		if (!homedir.exists()) {
			try {
				homedir.mkdirs();
			} catch (Exception ex) {
				throw new Exception("Can not mkdir :" + home + " Maybe include special charactor!");
			}
		}
	}

	/**
	 * This class copies an input files of event directory to another directory not
	 * include subdir
	 * 
	 * @param String
	 *            sourcedir the directory to copy from such as:/home/bqlr/images
	 * @param String
	 *            destdir the target directory
	 */
	public static void CopyDir(String sourcedir, String destdir) throws Exception {
		File dest = new File(destdir);
		File source = new File(sourcedir);

		String[] files = source.list();
		try {
			makehome(destdir);
		} catch (Exception ex) {
			throw new Exception("CopyDir:" + ex.getMessage());
		}

		for (int i = 0; i < files.length; i++) {
			String sourcefile = source + File.separator + files[i];
			String destfile = dest + File.separator + files[i];
			File temp = new File(sourcefile);
			if (temp.isFile()) {
				try {
					copy(sourcefile, destfile);
				} catch (Exception ex) {
					throw new Exception("CopyDir:" + ex.getMessage());
				}
			}
		}
	}

	/**
	 * This class del event directory recursively,that means delete all files and
	 * directorys.
	 * 
	 * @param File
	 *            directory the directory that will be deleted.
	 */
	public static void recursiveRemoveDir(File directory) throws Exception {
		if (!directory.exists())
			throw new IOException(directory.toString() + " do not exist!");

		String[] filelist = directory.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				recursiveRemoveDir(tmpFile);
			} else if (tmpFile.isFile()) {
				try {
					tmpFile.delete();
				} catch (Exception ex) {
					throw new Exception(tmpFile.toString() + " can not be deleted " + ex.getMessage());
				}
			}
		}
		try {
			directory.delete();
		} catch (Exception ex) {
			throw new Exception(directory.toString() + " can not be deleted " + ex.getMessage());
		} finally {
			filelist = null;
		}
	}

}

package com.bigid.web.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.bigid.exceptions.BusinessException;

public class CommonUtil {
	
	private static String imgBasePathDir = null;
	private static String staticResourceBasePathDir = null;
	
	public static String getLoggedInUsername(){
		return ((UserDetails)SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUsername().split(Constants.CEDILLA)[0];
	}
	
	public static long getLoggedInUserId(){
		return Long.parseLong(((UserDetails)SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUsername().split(Constants.CEDILLA)[1]);
	}

	public static Date getFormattedDate(String date) {
		try {
			Date parsedDate;
			DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
			if (!date.isEmpty()) {
				parsedDate = (Date) formatter1.parse(date);
				return parsedDate;
			}
		} catch (ParseException pe) {
			System.out.println("Exception :" + pe.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static String getImgBaseDirPath() {
		
		if(imgBasePathDir == null) {
			// fetch the upload base dir folder where images will be stored
			StringBuilder baseDirBuff = new StringBuilder(MessageResolver
					.getMessage(CommonProperties.PHOTOS_UPLOAD_BASEDIR, "/opt/")
					.replace(Constants.BACKSLASH, Constants.FORWARDSLASH));
	
			if (!baseDirBuff.toString().endsWith(
					String.valueOf(Constants.FORWARDSLASH))) {
				baseDirBuff.append(Constants.FORWARDSLASH);
			}
	
			// fetch subdirectory name for images to be stored on filesystem
			String subDirLocation = MessageResolver.getMessage(
					CommonProperties.SUB_IMG_UPLOAD_DIR, "bigid")
					.replace(Constants.BACKSLASH, Constants.FORWARDSLASH);
	
			baseDirBuff.append(subDirLocation);
	
			if (!baseDirBuff.toString().endsWith(
					String.valueOf(Constants.FORWARDSLASH))) {
				baseDirBuff.append(Constants.FORWARDSLASH);
			}
			
			String baseDir = baseDirBuff.toString().replace(Constants.BACKSLASH,
					Constants.FORWARDSLASH);
			
			if(baseDir.endsWith(Constants.FORWARDSLASH)){
				baseDir = baseDir.substring(0, baseDir.length()-1);
			}
			imgBasePathDir = baseDir;
		}

		return imgBasePathDir;
	}
	
	
	public static String getStaticResourceBaseDirPath() {
		
		if(staticResourceBasePathDir == null) {
			// fetch the upload base dir folder where images will be stored
			StringBuilder baseDirBuff = new StringBuilder(MessageResolver
					.getMessage(CommonProperties.STATIC_UPLOAD_BASEDIR, "/opt/")
					.replace(Constants.BACKSLASH, Constants.FORWARDSLASH));
	
			if (!baseDirBuff.toString().endsWith(
					String.valueOf(Constants.FORWARDSLASH))) {
				baseDirBuff.append(Constants.FORWARDSLASH);
			}
			
			String baseDir = baseDirBuff.toString().replace(Constants.BACKSLASH,
					Constants.FORWARDSLASH);
			
			if(baseDir.endsWith(Constants.FORWARDSLASH)){
				baseDir = baseDir.substring(0, baseDir.length()-1);
			}
			staticResourceBasePathDir = baseDir;
		}

		return staticResourceBasePathDir;
	}

	/**
	 * Function to parse the formatted String value as per the format specified
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date getFormattedDate(String date, String format) {
		try {
			Date parsedDate;
			DateFormat formatter = new SimpleDateFormat(format);
			if (!date.isEmpty()) {
				parsedDate = (Date) formatter.parse(date);
				return parsedDate;
			}
		} catch (ParseException pe) {
			throw new BusinessException("Unparseable date: " + date
					+ " with the following fomat: " + format);
		} catch (Exception e) {
			throw new BusinessException("Unparseable date: " + date
					+ " with the following fomat: " + format);
		}
		return null;
	}

	/**
	 * Method to return the formatted date value as String
	 * 
	 * @param dateArg
	 * @param dateFormat
	 * @return
	 */
	public static String getFormattedDateAsString(Date dateArg,
			String dateFormat) {
		try {
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			if (dateArg != null) {
				return formatter.format(dateArg);
			}
		} catch (Exception e) {
			throw new BusinessException("Unparseable date: " + dateArg
					+ " with the following fomat: " + dateFormat);
		}
		return null;
	}

	public static String getFormattedDateAsString(Date dateArg) {
		try {
			DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
			if (dateArg != null) {

				return formatter1.format(dateArg);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static String getMD5Hash(String path) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte[] data = path.getBytes();
			m.update(data, 0, data.length);
			BigInteger i = new BigInteger(1, m.digest());
			return String.format("%1$032X", i);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception while generating MD5 hash:"
					+ e.getMessage());
		}
		return null;
	}

	/**
	 * This method compares whether two objects are equal or not it trivially 1)
	 * Returns <code> true </code> if both parameters are <code> null </code> 2)
	 * Returns <code> false </code> if any one of the parameters is
	 * <code> null </code> 3) Does actual comparison between objects otherwise.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean compareObjects(Object source, Object target) {
		if (null == source && null == target) {
			return true;
		}
		if (null == source || null == target) {
			return false;
		}
		return source.equals(target);
	}

	/**
	 * Returns the extension of the specified file.
	 * 
	 * @param fileName
	 *            - Filename
	 * @return File extension
	 */
	public static String getFileExtension(final String fileName) {

		String extension = null;

		if (StringUtils.isEmpty(fileName)) {

			int indx = fileName.lastIndexOf(".");

			if (indx != -1) {
				extension = fileName.substring(indx + 1, fileName.length());
			}
		}
		return extension;
	}

	/**
	 * Return a directory path appended with file separator.
	 * 
	 * @param directory
	 *            - Directory path
	 * @return Directory path appended with file separator.
	 */
	public static String getDirectory(final String directory) {

		StringBuilder dir = new StringBuilder(directory);

		return directory.endsWith(String.valueOf(File.separatorChar)) ? dir
				.toString() : dir.append(File.separatorChar).toString();

	}

	public static Date getCurrentTime() {
		Date currentDate = Calendar.getInstance().getTime();
		return currentDate;

	}

	public static byte[] toByteArray(String fileLocation) throws IOException,Exception {

		if (fileLocation == null || fileLocation.trim().length() == 0) {
			throw new Exception(
					"File Location found NULL while trying to get Byte Array.");
		}
		
		File file = new File(fileLocation);
		
		if (!file.exists()) {
			throw new Exception("File Name: " + fileLocation
					+ " does not exists.");
		}
		
		return toByteArray(new FileInputStream(file));
	}
	
	public static byte[] toByteArray(InputStream is) throws IOException {
		byte[] buffer = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int n;
		baos.reset();

		while ((n = is.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, n);
		}

		return baos.toByteArray();
	}

	public static boolean isImageMimeTypeValid(List<String> supportedMimeTypes,
			String imageMime) {
		if (imageMime == null || imageMime.trim().length() == 0) {
			return false;
		}

		if (supportedMimeTypes == null || supportedMimeTypes.size() == 0) {
			return false;
		}

		String mimeType = null;
		for (int i = 0; i < supportedMimeTypes.size(); i++) {
			mimeType = supportedMimeTypes.get(i);
			if (mimeType != null && mimeType.trim().length() > 0) {
				if (imageMime.toUpperCase().indexOf(mimeType.toUpperCase()) != -1) {
					return true;
				}
			}
		}

		return false;
	}

}

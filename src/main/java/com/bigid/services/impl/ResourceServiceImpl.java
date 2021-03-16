package com.bigid.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bigid.exceptions.BusinessException;
import com.bigid.services.ResourceService;
import com.bigid.web.common.CommonProperties;
import com.bigid.web.common.CommonUtil;
import com.bigid.web.common.Constants;
import com.bigid.web.common.MessageResolver;
import com.bigid.web.common.SimpleImageInfo;

@Service
class ResourceServiceImpl implements ResourceService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String FORWARD_SLASH = "/";
	private static final String IMG_MAGIC_INSTALL_DIR_ERROR = "Image Magick Installation directory is invalid. Could not find the following file: %s";
	private static final String IMG_MAGIC_INSTALL_NOT_FOUND_ERROR = "Image Magick Location is NULL while trying to resize the image.";

	public ResourceServiceImpl() {

	}

	/**
	 * prepares hex based path i.e. folder structure to store resource
	 * 
	 * @param rootPath
	 * @param resourceId
	 * @return
	 */
	private String prepareDirectoryPath(String rootPath, String resourceId) {

		LOGGER.debug("Upload Resource : prepareDirectory : Preparing distinctive directory path : ResourceId"
				+ resourceId);
		// Getting MD5 hash based on resource name.
		final String hash = CommonUtil.getMD5Hash(resourceId);
		// hash = a21d68bc28915176a26e190b16024905
		final StringBuffer dirPath = new StringBuffer(hash);
		dirPath.append("_"+System.currentTimeMillis());
		dirPath.insert(6, FORWARD_SLASH);
		dirPath.insert(4, FORWARD_SLASH);
		dirPath.insert(2, FORWARD_SLASH);

		final String finalDirectoryPath = rootPath + FORWARD_SLASH
				+ dirPath.substring(0, 9);
		
		final File dir = new File(finalDirectoryPath);
		if (!dir.exists()) {
			dir.mkdirs();
			if (!dir.exists()) {
				throw new BusinessException("Unable to create directory..Path =" + dir.getAbsolutePath());
			}
		}
		LOGGER.debug("Upload Resource : prepareDirectory : Distinctive directory path : "
				+ dirPath.toString());
		return dirPath.toString();
	}

	@Override
	public boolean removeResource(String resourcePath,
			List<String> dimemsionList) {

		LOGGER.debug("Deleting resources " + resourcePath + " of dimension "
				+ dimemsionList);

		if (resourcePath == null || resourcePath.trim().isEmpty()) {
			throw new BusinessException(
					new Throwable(
							"Resource Location is found NULL while trying to remove resource from Filesystem"));
		}

		File masterResFile = new File(resourcePath);

		// find out the filename and extension of the resource
		String filename = masterResFile.getName();
		int indx = filename.lastIndexOf(Constants.DOT);

		if (indx != -1) {
			filename = filename.substring(0, indx);
		}

		if (!masterResFile.delete()) {
			LOGGER.warn("File: "
					+ masterResFile.getName()
					+ " cannot be deleted from the following location: "
					+ masterResFile.getAbsolutePath());
		} else {
			LOGGER.debug("File: " + masterResFile.getName()
					+ " deleted from the following location: "
					+ masterResFile.getAbsolutePath());
		}

		// get the parent path ex: resourcePath =
		// D:/opt/products/aa/bb/cc/xyz2222.jpg
		// parentPath below will be 'D:/opt/products/aa/bb/cc/'
		String parentPath = masterResFile.getParent();

		String resizeExtension = MessageResolver
			.getMessage(CommonProperties.IMAGE_UPLOAD_RESIZE_EXTENTION, "jpeg");
		
		if (!dimemsionList.isEmpty()) {
			String dimensionLocation = null;
			File fileToDelete = null;

			for (String dimension : dimemsionList) {
				if (dimension != null && !dimension.trim().isEmpty()) {

					// form the dimension wise resource name
					dimensionLocation = parentPath + File.separatorChar
							+ filename + "_" + dimension.trim() + "."
							+ resizeExtension;
					fileToDelete = new File(dimensionLocation);
					if (fileToDelete.exists()) {
						if (!fileToDelete.delete()) {
							LOGGER.warn("File: "
									+ fileToDelete.getName()
									+ " cannot be deleted from the following location: "
									+ fileToDelete.getAbsolutePath());
						} else {
							LOGGER.debug("File: " + fileToDelete.getName()
									+ " deleted from the following location: "
									+ fileToDelete.getAbsolutePath());
						}
					}
				}
			}// end of for loop

		}
		return true;
	}

	/*public String storeResourceForNewDimenstion(final InputStream resInpStream,
			String resourceIdentifier, String baseDir, String extension,
			List<String> dimemsionList) {
		
	}*/
	
	@Override
	public String storeResource(final InputStream resInpStream,
			String resourceIdentifier, String baseDir, String extension,
			List<String> dimemsionList) {

		LOGGER.debug("Upload Resource Service started.");
		String resourceURL = null;

		/*
		 * if (content == null) { throw new BusinessException( new Throwable(
		 * "Resource contents found NULL while trying to store resource.")); }
		 */

		// prepare hex based path i.e. folder structure to store images
		String imgHexPath = prepareDirectoryPath(baseDir.toString(),
				resourceIdentifier);

		// form the server filesystem based url for the original image
		resourceURL = baseDir.toString() + imgHexPath + Constants.DOT
				+ extension;

		resourceURL = resourceURL.replace(Constants.BACKSLASH,
				Constants.FORWARDSLASH);

		byte[] inputArray = getBytes(resInpStream);
		// write i.e. copy the original Image
		write(inputArray, resourceURL);
		File imageFile = new File(resourceURL);
		
		if(imageFile.exists()){
			//createDimensionForImage(imgHexPath,inputArray, baseDir, extension, dimemsionList, null);
		}
		
		LOGGER.debug("The final uploaded image url is " + resourceURL);
		return resourceURL;
	}
	
	public void createDimensionForImage(String imgHexPath, byte[] inputImageByteArray, String baseDir, String extension,List<String> dimemsionList, String module){
		if (!dimemsionList.isEmpty()) {
			// validate imagic magic third party configurations
			String imageMagickSWPath = validateImageMagicConfig();

			String dimensionLocation = null;
			String cmdCommand = null;
			//byte[] inputImageByteArray=null;
			//byte[] inputImageByteArray = getBytes(inputStream);
			
			//	inputImageByteArray= getBytes(imageInfo.getInputStream());
			
			
 			SimpleImageInfo imageInfo = new SimpleImageInfo(inputImageByteArray);					
			LOGGER.debug("Upload Resource Service : Primary image upload completed.");		
			
			String resizeExtension="";
			if(module!=null && "storeLogo".equals(module)){
				resizeExtension = extension;
			}else{
				resizeExtension = MessageResolver
				.getMessage(CommonProperties.IMAGE_UPLOAD_RESIZE_EXTENTION, "jpeg");
			}

			String imageCompressUpto = MessageResolver.getMessage(CommonProperties.IMAGE_COMPRESS_UPTO, "100%");

			// iterate the dimension list
			for (String dimension : dimemsionList) {
				if (dimension != null && !dimension.trim().isEmpty()) {

					// form file name and path for each dimension
					// i.e. for original xyz.jpg, below will form xyz_89X83.jpg
					// file on filesystem where 89x83 will be the dimension from
					// the dimensionList										
					dimensionLocation = baseDir.toString() + imgHexPath
							+ Constants.UNDERSCORE + dimension.trim()
							+ Constants.DOT + extension;

					// First copy the original image as it with different name
					// having suffix as dimension
					write(inputImageByteArray, dimensionLocation);

					File newImgFile = new File(dimensionLocation);
					if (newImgFile.exists()) {
						LOGGER.debug("File Created at this location: "
								+ newImgFile.getAbsolutePath());
						
						if(imageNeedsToBeResized(imageInfo.getWidth(), imageInfo.getHeight(), dimension)){
							// form the command line syntax for resizing the image
							cmdCommand = imageMagickSWPath + " -background white -flatten -format " + resizeExtension 
									+ " -resize "
									+ dimension.trim() + " -strip -quality " + imageCompressUpto +" -type optimize "
									+ newImgFile.getAbsolutePath();
							LOGGER.info("Executing command: " + cmdCommand);

							// invoke the command to resize the image as per the
							// dimension
							invokeResizeCommand(cmdCommand);
							LOGGER.debug("Image converted " + imgHexPath + "to "
									+ dimension);
							
							//deleting the original dimension file created initially
							//-format option creates a new image with new extension
							
							if(!extension.equalsIgnoreCase(resizeExtension)){//if added by Salman.khan, it was causing problem when the uploaded image is of extension .jpeg
								newImgFile.delete();
							}
							String resourceURL=baseDir+imgHexPath+"_"+dimension+"."+resizeExtension;
							File f=new File(resourceURL);
						}
						else {
							//if the uploaded Image is a jpeg image, dont change the extension
							if(!resizeExtension.equalsIgnoreCase(extension)){
								// form the command line syntax for resizing the image
								cmdCommand = imageMagickSWPath + " -format " + resizeExtension	+ " "								
										+ newImgFile.getAbsolutePath();
								LOGGER.info("Executing command: " + cmdCommand);

								// invoke the command to change the image format
								invokeResizeCommand(cmdCommand);
								LOGGER.debug("Image format changed to  " + resizeExtension);
								
								//deleting the original dimension file created initially
								//-format option creates a new image with new extension
								if(!extension.equalsIgnoreCase(resizeExtension)){
								newImgFile.delete();
								}
							}
						}							
												
					} else {
						throw new BusinessException(new Throwable(
								"Uploaded original image : " + imgHexPath
										+ " not found at the location : "
										+ newImgFile.getAbsolutePath()));
					}
				}
			}
		}
	}



		
	/**
	 * Function to check if Image needs to be resized
	 * Function checks if the uploaded Image width and height is less than the expected dimension
	 * if less, than don't resize else needs to be resized
	 * @param originalImageWidth
	 * @param originalImageHeight
	 * @param resizeDimension
	 * @return
	 */
	private boolean imageNeedsToBeResized(int originalImageWidth, int originalImageHeight, String resizeDimension) {
		
		if(resizeDimension != null && resizeDimension.trim().length() > 0){
			
			if(resizeDimension.toUpperCase().indexOf("X") == -1){
				throw new BusinessException("GENERIC_USERDEFINED_MESSAGE: "+ " Invalid Dimension Value: " + resizeDimension);
			}
			else {			

				String[] dimensions = resizeDimension.toUpperCase().split("X");
				if(dimensions != null && dimensions.length == 2) {
					
					try {
						int width = Integer.parseInt(dimensions[0].trim());
						int height = Integer.parseInt(dimensions[1].trim());
						
						if(originalImageWidth <= width && originalImageHeight <= height){
							return false;
						}
						else {
							return true;
						}
					}
					catch(NumberFormatException num) {
						throw new BusinessException("GENERIC_USERDEFINED_MESSAGE: "+"Invalid Dimension Value: " + resizeDimension + 
										". Dimension Height and width has to be integers.");
					}
				}
			}			
		}
		
		return false;		
	}
	
	/**
	 * Function to invoke resize command
	 * 
	 * @param cmd
	 * @throws Exception
	 */
	private void invokeResizeCommand(String cmd) {
		Process process = null;
		try {
			LOGGER.debug("Resizing Image : ");

			Runtime runTime = Runtime.getRuntime();
			process = runTime.exec(cmd);
			process.waitFor();
			int exitValue = process.exitValue();

			LOGGER.debug("Resize Image : Executed command :" + cmd
					+ ": The Exit Value : " + exitValue);

			if (exitValue != 0) {
				StringBuffer cmdErrMsg = new StringBuffer();
				InputStream errStream = process.getErrorStream();
				byte[] bytes = new byte[80];
				while (errStream.read(bytes) != -1) {
					cmdErrMsg.append(new String(bytes));
				}
				if (cmdErrMsg != null && !cmdErrMsg.toString().trim().isEmpty()) {

					if (cmdErrMsg.toString().contains(
							"error/mvg.c/ReadMVGImage")) {
						throw new BusinessException("ERROR_IMAGEUPLOAD_INVALID_IMAGE");
					} else {
						throw new BusinessException(new Throwable(
								"Exception occured while resizing the image : Reason : "
										+ cmdErrMsg.toString()));
					}
				}
			}
		} catch (IOException e) {
			throw new BusinessException(e);
		} catch (InterruptedException e) {
			throw new BusinessException(e);
		} finally {
			LOGGER.debug("Resize Image: Destroying process which has resized the image.");
			process.destroy();
		}
	}

	/**
	 * Function to copy the resource from the input stream to local file system.
	 * 
	 * @param input
	 * @param fileName
	 * @param resizeWidth
	 * @param resizeHeight
	 * @throws Exception
	 */

	private void write(byte[] input, String fileName) {
		OutputStream outStream = null;
		InputStream inpStream = null;
		try {
			outStream = new FileOutputStream(fileName);
			inpStream = new ByteArrayInputStream(input);

			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = inpStream.read(buf)) > 0) {
				outStream.write(buf, 0, len);
			}
		} catch (FileNotFoundException e) {
			throw new BusinessException(e);
		} catch (IOException e) {
			throw new BusinessException(e);
		} finally {
			try {
				if(outStream != null)
					outStream.close();
				if(inpStream != null)
					inpStream.close();
			} catch (IOException e) {
				throw new BusinessException(e);
			}

		}

	}

	/**
	 * Function to get byte array from Input Stream
	 * 
	 * @param inputStrm
	 * @return
	 * @throws IOException
	 */
	private byte[] getBytes(InputStream inputStrm) {

		byte[] byteArray = null;
		ByteArrayOutputStream baos = null;
		try {
			byte[] buffer = new byte[8192];
			baos = new ByteArrayOutputStream(2048);
			int length;
			baos.reset();
			while ((length = inputStrm.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, length);
			}
			byteArray = baos.toByteArray();
		} catch (IOException e) {
			throw new BusinessException(e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				throw new BusinessException(e);
			}
		}
		return byteArray;
	}

	@Override
	public byte[] getResource(String imagePath, String resourceDimension) {

		LOGGER.debug("Get Resource : " + imagePath + " : dimensions :"
				+ resourceDimension);

		if (imagePath == null || imagePath.trim().length() == 0) {
			throw new BusinessException(
					new Throwable(
							"Database Resource URL found NULL while trying to get resource."));
		}

		File originalResource = new File(imagePath);
		if (!originalResource.exists()) {
			throw new BusinessException(new Throwable(
					"Original Resource is not present at the following location: "
							+ imagePath));
		}

		int indx = imagePath.lastIndexOf(Constants.DOT);
		String extension = null;
		String fileName = null;

		if (indx != -1) {
			extension = imagePath.substring((indx + 1), imagePath.length());
			fileName = imagePath.substring(0, indx);
		}

		if (extension == null || extension.trim().length() == 0) {
			throw new BusinessException(new Throwable(
					"Original Resource Extension is not present at the following location: "
							+ imagePath));
		}
		
		String resizeExtension = MessageResolver
				.getMessage(CommonProperties.IMAGE_UPLOAD_RESIZE_EXTENTION, "jpeg");

		String fileNameWithDim = null;
		if (resourceDimension == null || resourceDimension.trim().length() == 0) {
			fileNameWithDim = imagePath;
		} else {
			fileNameWithDim = fileName + Constants.UNDERSCORE
					+ resourceDimension.trim() + Constants.DOT + resizeExtension;
		}

		LOGGER.debug("Get Resource : Opening input steam to file "
				+ fileNameWithDim);

		byte[] byteArr = null;

		File file = new File(fileNameWithDim);
		FileInputStream inpStream;
		try {
			inpStream = new FileInputStream(file);
			byteArr = getBytes(inpStream);
			inpStream.close();
		} catch (FileNotFoundException e) {
			throw new BusinessException(e);
		} catch (IOException e) {
			throw new BusinessException(e);
		}
		LOGGER.debug("Get Resource : Returning byte array of the image "
				+ fileNameWithDim);

		return byteArr;
	}

	/*
	 * Validate Image Magic software installation configuration and returns the
	 * filesystem path of the software tool.
	 */
	private String validateImageMagicConfig() {

		// identify the mogrify location
		String imgMagicLocation = MessageResolver.getMessage(
				CommonProperties.IMAGE_MAGICK_MOGRIFY_LOCATION, "mogrify");

		if (imgMagicLocation == null || imgMagicLocation.trim().isEmpty()) {
			throw new BusinessException(new Throwable(
					IMG_MAGIC_INSTALL_NOT_FOUND_ERROR));
		}

		File imageMagicSW = new File(imgMagicLocation);
		if (!imageMagicSW.exists()) {
			throw new BusinessException(new Throwable(
					String.format(IMG_MAGIC_INSTALL_DIR_ERROR,
							imageMagicSW.getAbsolutePath())));
		}
		return imageMagicSW.getAbsolutePath();
	}
	
	public byte[] getByteArray(File file){
		byte[] byteArr = null;
		FileInputStream inpStream = null;
		try {
			inpStream = new FileInputStream(file);
			byteArr = getBytes(inpStream);
			inpStream.close();
		} catch (FileNotFoundException e) {
			throw new BusinessException(e);
		} catch (IOException e) {
			throw new BusinessException(e);
		}finally{
			try {
				if(inpStream!=null)
				inpStream.close();
			} catch (IOException e) {
				throw new BusinessException(e);
			}
		}

		return byteArr;
	}
}

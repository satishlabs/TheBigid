package com.bigid.services;

import java.io.InputStream;
import java.util.List;

public interface ResourceService {

	/**
	 * Method to delete the resource from server filesystem.
	 * 
	 * @param path
	 *            image server path which is to be deleted
	 * @param dimemsionList
	 *            list of dimension which are to be deleted along with original
	 *            file
	 */
	boolean removeResource(String path, List<String> dimemsionList);

	/**
	 * Method to get Resource i.e. file as byte array
	 * 
	 * @param databasePath
	 *            image url as per database record
	 * @param resourceDimension 
	 *            dimension of image which you want to get 
	 * 
	 * @return byte array of the identified image as per dimension mentioned
	 */
	byte[] getResource(String imagePath, String resourceDimension);

	/**
	 * Store the uploaded image on server filesystem into folder structure
	 * formed using hex logic. Resize the images to given list of dimension.
	 * 
	 * @param content
	 *            Uploaded image inputstream
	 * @param resourceIdentifier
	 *            Unique string used to form folder structure for image storage
	 * @param extension
	 *            file extension type
	 * @param subDirLocation
	 *            base directory including the root folder (ex: for product /opt/products/ ) under which hex based
	 *            folders will be created
	 * @param dimemsionList
	 *            list of dimension acc. to which original image will be
	 *            restored
	 * @return
	 * 
	 * @throws Exception
	 */
	String storeResource(InputStream content, String resourceIdentifier,
			String baseDir, String extension, List<String> dimemsionList);

}
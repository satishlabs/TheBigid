package com.bigid.web.controllers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigid.exceptions.BusinessException;

import com.bigid.services.ResourceService;
import com.bigid.web.common.CommonProperties;
import com.bigid.web.common.CommonUtil;
import com.bigid.web.common.Constants;
import com.bigid.web.common.MessageResolver;
import com.bigid.web.common.SimpleImageInfo;

@SecureRestController
@RequestMapping(value = "/upload/*")
public class UploadController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ResourceService resourceManager;
	
	@PostMapping("/image") 
	//@ResponseBody
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/upload/uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get("E://temp//" + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/upload/uploadStatus";
        
    }
	
	
	@GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

	@RequestMapping(value="/add/image", method=RequestMethod.POST)
	public ResponseEntity upload(@RequestParam("id") Long id, HttpServletResponse response, HttpServletRequest request)
	{   
		try {
			MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
			Iterator<String> it=multipartRequest.getFileNames();
			MultipartFile multipart=multipartRequest.getFile(it.next());
			String fileName=id+".png";
			String imageName = fileName;

			byte[] bytes=multipart.getBytes();
			BufferedOutputStream stream= new BufferedOutputStream(new FileOutputStream("src/main/resources/static/image/book/"+fileName));;

			stream.write(bytes);
			stream.close();
			return new ResponseEntity("upload success", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Upload fialed", HttpStatus.BAD_REQUEST);
		}   
	}
	
	
	@RequestMapping(value = "/image/{category}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadImage(@RequestParam("file") MultipartFile uploadedImageFile,
			@PathVariable String category){
		String imgURL = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			
			if (uploadedImageFile != null && !uploadedImageFile.isEmpty()) {

				log.info("uploading product image :"+ uploadedImageFile.getOriginalFilename() + ": for type "+ category);

				this.validateImage(uploadedImageFile.getSize(),uploadedImageFile.getOriginalFilename());
				
				SimpleImageInfo imageInfo = new SimpleImageInfo(uploadedImageFile.getOriginalFilename(),
						uploadedImageFile.getSize(),uploadedImageFile.getInputStream());
				imageInfo.setImageSubCategory(category);
				String imageBaseDirPath = CommonUtil.getImgBaseDirPath();
				// call service layer to upload the image to server filesystem
				imgURL = this.uploadImage(imageBaseDirPath,imageInfo);
				// generate URLs for thumbnail images of image fetched from url to be shown on UI.
				if (imageInfo != null) {
					resultMap.put("imgwidth",String.valueOf(imageInfo.getWidth()));
					resultMap.put("imgheight",String.valueOf(imageInfo.getHeight()));
				}
				//remove the base upload directory location from image url
				imgURL = imgURL.replace(imageBaseDirPath, Constants.BLANK);
				resultMap.put("imgURL", imgURL);
				resultMap.put("status", "success");
				
				log.info("Upload Product Image : Successfully :"
						+ " : for " + category + " The JSON response"
						+ resultMap.toString());

			} else { // this is executed when image uploaded is of size 0 kb.
				throw new BusinessException("ERROR_IMAGEUPLOAD_IMAGE_EMPTY : " + uploadedImageFile.getOriginalFilename());
			}

		} catch (BusinessException be) {
			resultMap.put("status", "failed");
			resultMap.put("reason", be.getMessage());
			log.info("Product image uploading failed :" + uploadedImageFile.getOriginalFilename() + " Reason : " + be.getMessage());
			
		} catch (Exception e) {
			resultMap.put("status", "failed");
			resultMap.put("reason", e.getMessage());
			// though service layer is throwing Exception wrapped as BusinessException, still the Runtime exception needs to be
			// caught since the request is AJAX and we are not redirecting to error page, so handling explicit exception
			e.printStackTrace();
			throw new BusinessException(e);
		} finally {
			try {
				uploadedImageFile.getInputStream().close();
				// write the generated response back to client in JSON format
			} catch (IOException e) {
				
			}
		}
		return resultMap;
	}
	
	
	private String uploadImage(String imageBaseDirPath, SimpleImageInfo image) {
		
		String imageUri = null;
		log.debug("Uploading Image Service started ...");
		if (image.getSize() > 0) {

			// find out the file name and extension
			String fileName = image.getImageName();
			int indx = fileName.lastIndexOf(Constants.DOT);
			String extension = null;
			if (indx != -1) {
				extension = fileName.substring(indx + 1, fileName.length());
				if(fileName.contains("&")) {
					extension = fileName.substring(indx + 1,fileName.indexOf("&"));
				}
				fileName = fileName.substring(0, indx);
			}

			// form a complex resource name in order to store the image in
			// distinctive folder on filesystem
			String resourceName = Constants.UNDERSCORE
					+ System.currentTimeMillis() + (Math.random() * 1000);

			log.debug("Upload Image : Resource Identifier formed is " + resourceName);

			String baseDirForUpload = imageBaseDirPath.concat(Constants.FORWARDSLASH) 
					+ (StringUtils.isEmpty(image.getImageSubCategory())? "" : image.getImageSubCategory().concat(Constants.FORWARDSLASH));

			log.debug("Upload Image : directory location formed " + baseDirForUpload);

			List<String> dimemsionList=new ArrayList<String>();
			log.debug("Upload Image : calling resource service to upload image.");
			
			// call the storage API to actually store and resize the image.
			imageUri = resourceManager.storeResource(image.getInputStream(),
					resourceName, baseDirForUpload, extension,
					dimemsionList);
		}

		return imageUri;
	}


	private void validateImage(Long imageSize,String imageName) {

		final long size = (imageSize / 1024);

		final int maxSize = Integer.parseInt(MessageResolver.getMessage(
				CommonProperties.PRODUCT_IMG_MAXSIZE, "200"));

		// check the size of image against permitted size
		if (size > maxSize) {
			throw new BusinessException("ERROR_IMAGEUPLOAD_SIZE EXCEEDED" + maxSize);
		}

		String strValue = MessageResolver.getMessage(
				CommonProperties.PRODUCT_IMG_CONTENTTYPE, "jpeg,png,jpg");

		String[] tokens = strValue.toUpperCase().split(Constants.COMMA);

		final List<String> contentTypes = Arrays.asList(tokens);

		if (!contentTypes.isEmpty()) {

			final String fileName = imageName;

			String extension = null;
			if (fileName.lastIndexOf(Constants.DOT) != -1) {
				extension = fileName.substring(
						(fileName.lastIndexOf(Constants.DOT) + 1),
						fileName.length());
			}

			// check the uploaded image extension against list of permitted file
			// types
			if (extension == null || extension.trim().length() == 0) {
				throw new BusinessException("ERROR_IMAGEUPLOAD_NOEXT_MESSAGE missing " + imageName);
			}

			// check the uploaded image content type
			if (!CommonUtil.isImageMimeTypeValid(contentTypes,
					extension.toUpperCase(Locale.getDefault()))) {

				throw new BusinessException("ERROR_IMAGEUPLOAD_INVALIDFORMAT " + extension.toUpperCase());
			}
		}

		log.debug("Image :"	+ imageName + " of size :" + size + " : validated successfully ");
	}

}

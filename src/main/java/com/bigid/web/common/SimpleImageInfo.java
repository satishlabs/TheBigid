package com.bigid.web.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bigid.exceptions.BusinessException;

public class SimpleImageInfo {
	private int height;
	private int width;
	private String mimeType;
	private byte[] imageInBytes;	
	private String imageName;
	private long size;
	private InputStream inputStream;
	private String extension;
	private String imageSubCategory;
	
	public SimpleImageInfo(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		try {
			processStream(is);
		} finally {
			is.close();
		}
	}

	public SimpleImageInfo(InputStream is) throws IOException {
		processStream(is);
	}

	public SimpleImageInfo(byte[] bytes) {
		
		InputStream is = null;
		
		try {
			setImageInBytes(bytes);
			
			try {
				is = new ByteArrayInputStream(bytes);
				processStream(is);
			}
			finally{
				if(is != null){
					is.close();
				}
			}			
		}
		catch(IOException ex){						
			throw new BusinessException(ex.getMessage());
		}
	}

	public SimpleImageInfo(String imageName, long size, InputStream inputStream) {
		this.imageName = imageName;
		this.size = size;
		this.inputStream = inputStream;
	}

	public SimpleImageInfo(String imageName, String extension, long size, InputStream inputStream) {
		this(imageName, size, inputStream);
		this.size = size;
	}
	
	private void processStream(InputStream is) throws IOException {
		int c1 = is.read();
		int c2 = is.read();
		int c3 = is.read();

		if(imageInBytes == null) {
			imageInBytes = CommonUtil.toByteArray(is);
		}
		
		mimeType = null;
		width = height = -1;

		if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
			is.skip(3);
			width = readInt(is,2,false);
			height = readInt(is,2,false);
			mimeType = "image/gif";
		} else if (c1 == 0xFF && c2 == 0xD8) { // JPG
			while (c3 == 255) {
				int marker = is.read();
				int len = readInt(is,2,true);
				if (marker == 192 || marker == 193 || marker == 194) {
					is.skip(1);
					height = readInt(is,2,true);
					width = readInt(is,2,true);
					mimeType = "image/jpeg";
					break;
				}
				is.skip(len - 2);
				c3 = is.read();
			}
		} else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
			is.skip(15);
			width = readInt(is,2,true);
			is.skip(2);
			height = readInt(is,2,true);
			mimeType = "image/png";
		} else if (c1 == 66 && c2 == 77) { // BMP
			is.skip(15);
			width = readInt(is,2,false);
			is.skip(2);
			height = readInt(is,2,false);
			mimeType = "image/bmp";
		} else {
			int c4 = is.read();
			if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
					|| (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
				boolean bigEndian = c1 == 'M';
				int ifd = 0;
				int entries;
				ifd = readInt(is,4,bigEndian);
				is.skip(ifd - 8);
				entries = readInt(is,2,bigEndian);
				for (int i = 1; i <= entries; i++) {
					int tag = readInt(is,2,bigEndian);
					int fieldType = readInt(is,2,bigEndian);
					//long count = readInt(is,4,bigEndian);
					int valOffset;
					if ((fieldType == 3 || fieldType == 8)) {
						valOffset = readInt(is,2,bigEndian);
						is.skip(2);
					} else {
						valOffset = readInt(is,4,bigEndian);
					}
					if (tag == 256) {
						width = valOffset;
					} else if (tag == 257) {
						height = valOffset;
					}
					if (width != -1 && height != -1) {
						mimeType = "image/tiff";
						break;
					}
				}
			}
		}
		if (mimeType == null) {
			throw new IOException("Unsupported image type");
		}
	}
	
	private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
		int ret = 0;
		int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
		int cnt = bigEndian ? -8 : 8;
		for(int i=0;i<noOfBytes;i++) {
			ret |= is.read() << sv;
			sv += cnt;
		}
		return ret;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String toString() {
		return "MIME Type : " + mimeType + "\t Width : " + width + "\t Height : " + height; 
	}

	/**
	 * @param imageInBytes the imageInBytes to set
	 */
	public void setImageInBytes(byte[] imageInBytes) {
		this.imageInBytes = imageInBytes;
	}

	/**
	 * @return the imageInBytes
	 */
	public byte[] getImageInBytes() {
		return imageInBytes;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getImageSubCategory() {
		return imageSubCategory;
	}
	public void setImageSubCategory(String imageSubCategory) {
		this.imageSubCategory = imageSubCategory;
	}
}
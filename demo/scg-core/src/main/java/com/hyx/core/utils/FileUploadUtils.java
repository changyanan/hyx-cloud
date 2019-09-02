package com.hyx.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hyx.core.exception.GlobalException;

public class FileUploadUtils {

	private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);

	/**
	 * 上传文件到指定的目录
	 * @param checkFile
	 * @param rootFolder
	 * @return
	 */
	public static File upload(MultipartFile checkFile, File rootFolder ) {
		return upload(checkFile, rootFolder, null);
	}
	
	/**
	 * 接收上传文件到指定目录，并返回相对上传跟目录的路径
	 * @param checkFile
	 * @param rootFolder
	 * @param folder
	 * @return
	 */
	public static String uploadGetRelative(MultipartFile checkFile, File rootFolder, String folder) {
		File sourceFile = upload(checkFile, rootFolder, folder);
		String absoluteRoot=rootFolder.getAbsolutePath();
		String absoluteFile=sourceFile.getAbsolutePath();
		return absoluteFile.substring(absoluteRoot.length());
	}
	
	/**
	 * 上传文件到指定的目录
	 * @param checkFile
	 * @param rootFolder
	 * @param folder
	 * @return
	 */
	public static File upload(MultipartFile checkFile, File rootFolder, String folder) {
		if(StringUtils.isEmpty(folder)) {
			folder="";
		}
		File destFolder = new File(rootFolder, folder);
		if (!destFolder.exists()) {
			destFolder.mkdirs();
			log.debug("创建目录 {}完毕", destFolder);
		}
	
		File dest = new File(destFolder, newFileName(checkFile.getOriginalFilename()));
		try {
			checkFile.transferTo(dest);
			log.info("上传文件完毕  >>> {}" ,dest);
		} catch (IllegalStateException | IOException e) {
			log.error("上传文件{}失败" ,dest, e);
			GlobalException.error("上传文件失败");
		}
		return dest;
	}
	private  static String newFileName(String originalFilename) {
		String fileName;
		if(StringUtils.isEmpty(originalFilename)) {
			fileName=UUID.randomUUID().toString();
		}else {
			int index=originalFilename.lastIndexOf('.');
			if(index<=0) {
				fileName=UUID.randomUUID().toString();
			}else {
				fileName=UUID.randomUUID().toString()+originalFilename.substring(index);
			}
		}
		return fileName.replaceAll("\\-", "");
	}

	public static String uploadGetRelative(String checkFileName, File rootFolder, String folder) {
		File checkFile=new File(rootFolder, checkFileName);
		Assert.isTrue(checkFile.isFile(), "对账文件不存在");
		if(StringUtils.isEmpty(folder)) {
			folder="";
		}
		File destFolder = new File(rootFolder, folder);
		if (!destFolder.exists()) {
			destFolder.mkdirs();
			log.debug("创建目录 {}完毕", destFolder);
		}
		File sourceFile=new File(destFolder, newFileName(checkFileName));
		try {
			FileCopyUtils.copy(checkFile, sourceFile);
			checkFile.delete();
			log.info("移动文件完毕  {}  ==>> {}" ,checkFile, sourceFile );
		} catch (IOException e) {
			log.warn("移动文件失败  {}  ==>> {}" ,checkFile, sourceFile ,e);
			GlobalException.warn("文件移动失败");
		}
		String absoluteRoot=rootFolder.getAbsolutePath();
		String absoluteFile=sourceFile.getAbsolutePath();
		return absoluteFile.substring(absoluteRoot.length());
	}
	
}

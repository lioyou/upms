package com.leecp.upms.admin.util;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.util.UUID;

import org.apache.shiro.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import com.leecp.upms.common.util.PropertiesFileUtil;

public class FileUploadUtil {
	/**
	 * 存储上传的文件
	 * @param file
	 * @return 文件名的前缀
	 */
	public static String upload(MultipartFile file){
		 if (!file.isEmpty()) {
			 	String filePrefix = "";
	            try {
					//文件原始名称
					String fileName = file.getOriginalFilename();
					if(fileName != null && fileName.length()>0){
						String savePath = PropertiesFileUtil.getInstance("upms-server").get("filePath");
						filePrefix = UUID.randomUUID().toString().replaceAll("-","");
						//文件的新名称
						String relativePath = filePrefix + "-" + fileName;
						File saveFile = new File(savePath+relativePath);
						file.transferTo(saveFile);
					}else{//上传操作失败
						return null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //返回文件名称
	            return filePrefix;
	        }
		return null;
	}
}

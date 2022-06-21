package com.douzone.server.config.utils;


import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.douzone.server.config.s3.AwsS3;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.exception.ImgFileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadUtils {
	@Value(value = "${aws-client.path}")
	private String awsPath;
	private final AwsS3 awsS3;


	public List<UploadDTO> upload(List<MultipartFile> files, String basePath) {
		if (files.isEmpty() || files == null) {
			throw new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND);
		}
		List<String> fileName = new ArrayList<>();
		List<String> fileType = new ArrayList<>();
		List<Long> fileLength = new ArrayList<>();
		List<String> filePath = new ArrayList<>();
		List<UploadDTO> uploadDTOS = new ArrayList<>();

		try {
			for (int i = 0; i < files.size(); i++) {
				try {
					fileName.add(LocalDateTime.now() + "_" + files.get(i).getOriginalFilename());
					fileType.add(files.get(i).getContentType());
					fileLength.add(files.get(i).getSize());
					// 업로드 될 버킷 객체 url
					filePath.add(awsS3.upload(files.get(i), basePath + fileName.get(i), fileType.get(i), fileLength.get(i)));
					uploadDTOS.add(UploadDTO.builder().path(awsPath + filePath.get(i)).type(fileType.get(i)).imgSize(String.valueOf(fileLength.get(i))).build());
				} catch (AmazonS3Exception e) {
					log.error("AmazonS3Exception : UploadUtils - upload " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					log.error("IOException : UploadUtils - upload " + e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					log.error("Exception : UploadUtils - upload " + e.getMessage());
					e.printStackTrace();
				}
			}
			return uploadDTOS;
		} catch (IllegalStateException e) {
			log.error("IllegalStateException : UploadUtils - upload " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Exception : UploadUtils - upload " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void delete(List<String> CurrentImgPath) {

		List<String> DelImgPathKey = new ArrayList<>();

		for (int i = 0; i < CurrentImgPath.size(); i++) {
			try {
				if (!(CurrentImgPath.get(i) == null)
				) {
					DelImgPathKey.add(CurrentImgPath.get(i).split("/")[3] + "/" + CurrentImgPath.get(i).split("/")[4]);
					awsS3.delete(DelImgPathKey.get(i));
				}
			} catch (AmazonS3Exception e) {
				log.error("AmazonS3Exception : UploadUtils - delete " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.error("Exception : UploadUtils - delete " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public AwsS3 getAwsS3() {
		return this.awsS3;
	}

	public String getAwsPath() {
		return this.awsPath;
	}
}

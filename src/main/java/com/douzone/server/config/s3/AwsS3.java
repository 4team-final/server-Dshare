package com.douzone.server.config.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class AwsS3 {
    //Amazon-s3-sdk
    private AmazonS3 s3Client;
    private Regions clientRegion = Regions.AP_NORTHEAST_2;
    @Value(value = "${aws-client.bucket}")
    private String bucketName;

    private AwsS3() {
        this.createS3Client();
    }

    //    singleton pattern
    static private AwsS3 instance = null;

    public static AwsS3 getInstance() {
        if (instance == null) {
            return new AwsS3();
        } else {
            return instance;
        }
    }

    //aws S3 client 생성
    private void createS3Client() {
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new EnvironmentVariableCredentialsProvider()).withRegion(clientRegion).build();
    }

    //File과 key로 PutObjectRequest
    public void upload(File file, String key) {
        uploadToS3(new PutObjectRequest(this.bucketName, key, file));
    }

    //MultipartFile로 InputStream을 이용하여 PutObjectRequest
    public String upload(MultipartFile multipartFile, String key, String contentType, long contentLength) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(contentLength);
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, key, multipartFile.getInputStream(), objectMetadata);
        uploadToS3(putObjectRequest);
        return putObjectRequest.getKey();
    }

    //S3Client로 aws S3 버킷에 업로드
    private void uploadToS3(PutObjectRequest putObjectRequest) {
        try {
            this.s3Client.putObject(putObjectRequest);
            log.info(putObjectRequest.getKey() + "upload complete");
            System.out.println(String.format("[%s] upload complete", putObjectRequest.getKey()));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(String orgKey, String copyKey) {
        try {
            //Copy 객체 생성
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(this.bucketName, orgKey, this.bucketName, copyKey);
            this.s3Client.copyObject(copyObjRequest);
            log.info("Finish copying" + orgKey + "to" + copyKey);
            System.out.println(String.format("Finish copying [%s] to [%s]", orgKey, copyKey));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void delete(String key) {
        try {
            //Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucketName, key);
            this.s3Client.deleteObject(deleteObjectRequest);
            log.info(key + "deletion complete");
            System.out.println(String.format("[%s] deletion complete", key));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
}
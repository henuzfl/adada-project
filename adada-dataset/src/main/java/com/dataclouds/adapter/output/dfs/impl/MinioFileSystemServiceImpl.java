package com.dataclouds.adapter.output.dfs.impl;

import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Author: zfl
 * @Date: 2021/3/4 11:52
 * @Version: 1.0.0
 */
@Service
public class MinioFileSystemServiceImpl implements IFileSystemService {

    @Autowired
    private MinioProperties minioProperties;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            minioClient = new MinioClient(minioProperties.getEndpoint(),
                    minioProperties.getAccessKey(),
                    minioProperties.getSecretKey());
            if (!minioClient.bucketExists(minioProperties.getBucket())) {
                minioClient.makeBucket(minioProperties.getBucket());
            }
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (RegionConflictException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String upload(String fileName, InputStream inputStream) {
        String objectName =
                UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
        try {
            minioClient.putObject(minioProperties.getBucket(), objectName,
                    inputStream,
                    new PutObjectOptions(inputStream.available(), -1));
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return minioProperties.getBucket() + "/" + objectName;
    }

    @Override
    public String getUrl(String fsPath) {
        return minioProperties.getEndpoint() + fsPath;
    }
}

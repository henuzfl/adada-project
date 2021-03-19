package com.dataclouds.adapter.output.dfs.impl;

import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minioProperties.getBucket() + "/" + objectName;
    }

    @Override
    public String getUrl(String fsPath) {
        return minioProperties.getEndpoint() + fsPath;
    }
}

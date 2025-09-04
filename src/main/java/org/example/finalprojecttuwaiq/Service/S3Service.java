package org.example.finalprojecttuwaiq.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

@Service
public class S3Service {

    private S3Client s3;

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretAccessKey}")
    private String secretKey;



    @PostConstruct
    public void init() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3 = S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }



    public void uploadFile(String key,String filepath){
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket("ba-copilot-documents-generation")
                        .contentType("application/pdf")
                        .key(key)
                        .build(),
                Paths.get(filepath)

        );
    }



}

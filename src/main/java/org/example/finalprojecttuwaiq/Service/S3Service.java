package org.example.finalprojecttuwaiq.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.nio.file.Paths;
import java.time.Duration;

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

    public String generatePresignedUrl(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("ba-copilot-documents-generation")
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(7)) // 10 minutes validity
                    .getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        }
    }




}

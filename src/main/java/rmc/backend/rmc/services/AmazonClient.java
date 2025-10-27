package rmc.backend.rmc.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl:s3.ap-southeast-1.amazonaws.com}")
    private String endpointUrl;
    
    @Value("${amazonProperties.bucketName:default-bucket}")
    private String bucketName;
    
    @Value("${amazonProperties.accessKey:}")
    private String accessKey;
    
    @Value("${amazonProperties.secretKey:}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        try {
            if (accessKey.isEmpty() || secretKey.isEmpty()) {
                System.out.println("Warning: AWS credentials not configured. File uploads will not work.");
                System.out.println("Please configure amazonProperties.accessKey and amazonProperties.secretKey in application-local.properties");
                return;
            }

            BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            this.s3client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.AP_SOUTHEAST_1)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
            
            System.out.println("AWS S3 client initialized successfully with endpoint: " + endpointUrl);
        } catch (Exception e) {
            System.out.println("Error initializing AWS S3 client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        if (s3client == null) {
            throw new RuntimeException("AWS S3 client not initialized. Please check your AWS credentials configuration.");
        }
        
        try {
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }

    public String uploadFile(String avatarBase64) {
        if (s3client == null) {
            throw new RuntimeException("AWS S3 client not initialized. Please configure AWS credentials in application-local.properties");
        }

        final String[] base64Array = avatarBase64.split(",");
        String dataUir, data;
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            dataUir = "data:image/jpg;base64";
            data = base64Array[0];
        }
        MultipartFile multipartFile = new Base64ToMultipartFile(data, dataUir);
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = "https://" + bucketName + "." + endpointUrl + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process and upload file: " + e.getMessage(), e);
        }
        return fileUrl;
    }

    public String uploadPdfFile(MultipartFile multipartFile) {
        if (s3client == null) {
            throw new RuntimeException("AWS S3 client not initialized. Please configure AWS credentials in application-local.properties");
        }

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload PDF file: " + e.getMessage(), e);
        }
        return fileUrl;
    }

}

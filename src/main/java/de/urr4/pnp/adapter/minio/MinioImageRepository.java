package de.urr4.pnp.adapter.minio;

import de.urr4.pnp.domain.image.Image;
import de.urr4.pnp.domain.image.ImageRepository;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class MinioImageRepository implements ImageRepository {

    private final String bucketName = "images";

    private final MinioClient minioClient;

    public MinioImageRepository(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize Minio Bucket", e);
        }
    }

    @Override
    public UUID saveImage(Image image) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getImageBytes());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(image.getImageId().toString())
                            .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                            .contentType("image/png")
                            .build());
            return image.getImageId();
        } catch (Exception e) {
            throw new RuntimeException("Could not save image " + image.getImageId(), e);
        }
    }

    @Override
    public byte[] getImageById(UUID imageId) {
        try {
            GetObjectResponse getObjectResponse = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageId.toString())
                            .build());
            return getObjectResponse.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Could not load image " + imageId, e);
        }
    }
}

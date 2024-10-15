package de.urr4.pnp.adapter.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    private final String minioUrl;
    private final String minioUsername;
    private final String minioPassword;

    public MinioConfig(
            @Value("${minio.url}") String minioUrl,
            @Value("${minio.access.name}") String minioUsername,
            @Value("${minio.access.secret}") String minioPassword) {
        this.minioUrl = minioUrl;
        this.minioUsername = minioUsername;
        this.minioPassword = minioPassword;
    }


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioUsername, minioPassword)
                .build();
    }
}

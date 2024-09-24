package de.urr4.monsters.adapter.web;

import de.urr4.monsters.domain.image.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/images")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping(path = "/{id}")
    @CrossOrigin(exposedHeaders = "Image-Id")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") String id) {
        log.info("Loading image {}", id);
        byte[] imageBytes = imageRepository.getImageById(UUID.fromString(id));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Image-Id", id);
        headers.set(HttpHeaders.CONTENT_TYPE, "image/png");
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}

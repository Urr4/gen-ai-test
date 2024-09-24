package de.urr4.monsters.adapter.openai;

import de.urr4.monsters.domain.image.Image;
import de.urr4.monsters.domain.image.ImageGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
public class OpenAiImageGenerator implements ImageGenerator {

    private final OpenAiImageModel openAiImageModel;
    private final OpenAIMonsterImagePromptOptimizer promptOptimizer;

    private final String model;
    private final int imageWidth;
    private final int imageHeight;

    public OpenAiImageGenerator(
            @Value("${image.model}") String model,
            @Value("${image.height}") int height,
            @Value("${image.width}") int width,
            OpenAiImageModel openAiImageModel,
            OpenAIMonsterImagePromptOptimizer promptOptimizer) {
        this.model = model;
        this.imageHeight = height;
        this.imageWidth = width;
        this.openAiImageModel = openAiImageModel;
        this.promptOptimizer = promptOptimizer;
    }

    @Override
    public Image generateImage(String prompt) {
        String updatedPrompt = "Create a Fantasy Monster as if photographed in its natural habitat. "+ prompt;
        String optimizedPrompt = promptOptimizer.optimizePrompt(updatedPrompt);
        log.info("Generating image with prompt {}", optimizedPrompt);
        OpenAiImageOptions openAiImageOptions = OpenAiImageOptions.builder()
                .withN(1)
                .withModel(this.model)
                .withHeight(this.imageHeight)
                .withWidth(this.imageWidth)
                .build();

        ImageResponse imageResponse = openAiImageModel.call(new ImagePrompt(optimizedPrompt, openAiImageOptions));
        log.info("Generated image at {}", imageResponse.getResult().getOutput().getUrl());
        try {
            URL url = new URL(imageResponse.getResult().getOutput().getUrl());
            BufferedImage image = ImageIO.read(url);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            return new Image(UUID.randomUUID(), byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to download image", e);
        }
    }
}

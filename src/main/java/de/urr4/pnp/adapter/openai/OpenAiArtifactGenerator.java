package de.urr4.pnp.adapter.openai;

import de.urr4.pnp.domain.artifact.Artifact;
import de.urr4.pnp.domain.artifact.ArtifactGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class OpenAiArtifactGenerator implements ArtifactGenerator {

    private final BeanOutputConverter<Artifact> artifactBeanOutputConverter;
    private final ChatClient chatClient;

    public OpenAiArtifactGenerator(ChatClient chatClient) {
        this.artifactBeanOutputConverter = new BeanOutputConverter<>(Artifact.class);
        this.chatClient = chatClient;
    }

    @Override
    public Artifact generateArtifact(String prompt) {
        Artifact artifact = chatClient.prompt()
                .system("You are an experienced DnD master, able to homebrew new artifacts on the fly. You are very good and making interesting artifacts by their use.")
                .user(u -> u.text("Generate a new artifact with the following use: {prompt}. Generate the output only in format {format}, if you can't simply respond 'I can't do this'")
                        .param("format", artifactBeanOutputConverter.getFormat())
                        .param("prompt", prompt))
                .call()
                .entity(Artifact.class);

        //Reset uuid and imageIds as we dont want this generated
        artifact.setUuid(null);
        artifact.setImageIds(new ArrayList<>());
        return artifact;
    }

}

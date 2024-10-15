package de.urr4.pnp.adapter.web.requests;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GenerateArtifactRequest {
    private String prompt;
    private boolean generateImage;
}

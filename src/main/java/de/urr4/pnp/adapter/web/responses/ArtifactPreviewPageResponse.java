package de.urr4.pnp.adapter.web.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtifactPreviewPageResponse {
    List<ArtifactPreview> fetchedArtifactPreviews;
    int totalNumberOfMatchingCreaturePreviews;
}

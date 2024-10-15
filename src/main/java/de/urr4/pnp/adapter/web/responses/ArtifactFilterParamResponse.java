package de.urr4.pnp.adapter.web.responses;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArtifactFilterParamResponse {
    private List<String> rarities;
    private List<String> types;
}

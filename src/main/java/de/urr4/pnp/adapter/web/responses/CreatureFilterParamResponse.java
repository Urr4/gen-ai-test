package de.urr4.pnp.adapter.web.responses;

import de.urr4.pnp.domain.IntRange;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreatureFilterParamResponse {
    private IntRange armorClassRange;
    private IntRange hitPointRange;
    private List<String> currentTypes;
    private List<String> currentSizes;
}

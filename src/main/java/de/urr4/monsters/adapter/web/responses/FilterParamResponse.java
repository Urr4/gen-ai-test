package de.urr4.monsters.adapter.web.responses;

import de.urr4.monsters.domain.IntRange;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FilterParamResponse {
    IntRange armorClassRange;
    IntRange hitPointRange;
    List<String> currentTypes;
    List<String> currentSizes;
}

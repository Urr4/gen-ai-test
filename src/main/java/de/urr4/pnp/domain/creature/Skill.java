package de.urr4.pnp.domain.creature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill {
    private String name;
    private String modifier;
}

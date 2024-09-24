package de.urr4.monsters.domain.monster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Characteristic {
    private String name;
    private String description;
}
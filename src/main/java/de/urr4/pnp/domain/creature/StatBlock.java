package de.urr4.pnp.domain.creature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatBlock<T> {
    @With
    private T strength;
    @With
    private T dexterity;
    @With
    private T constitution;
    @With
    private T intelligence;
    @With
    private T wisdom;
    @With
    private T charisma;
}

package de.urr4.pnp.domain.creature;

import de.urr4.pnp.domain.IntRange;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Builder
@ToString
public class CreatureFilter {
    private String nameIncludes;
    private String size;
    private String type;
    private IntRange armorClassRange;
    private IntRange hitPointRange;
    private int pageNumber;
    private int pageSize;

    public void normalize() {
        if (!Objects.isNull(nameIncludes)) {
            this.nameIncludes = this.nameIncludes.toLowerCase();
        }
        if (!Objects.isNull(size)) {
            this.size = this.size.toLowerCase();
        }
        if (!Objects.isNull(type)) {
            this.type = this.type.toLowerCase();
        }
    }
}

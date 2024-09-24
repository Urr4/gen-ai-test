package de.urr4.monsters.adapter.web.requests;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GenerateMonsterRequest {
    private String prompt;
    private boolean generateImage;
}

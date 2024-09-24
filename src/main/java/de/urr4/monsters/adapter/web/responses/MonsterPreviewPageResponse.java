package de.urr4.monsters.adapter.web.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonsterPreviewPageResponse {

    List<MonsterPreview> fetchedMonsterPreviews;
    int totalNumberOfMatchingMonsterPreviews;

}

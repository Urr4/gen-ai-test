package de.urr4.monsters.adapter.web;

import de.urr4.monsters.adapter.web.requests.GenerateMonsterRequest;
import de.urr4.monsters.adapter.web.responses.FilterParamResponse;
import de.urr4.monsters.adapter.web.responses.MonsterPreview;
import de.urr4.monsters.adapter.web.responses.MonsterPreviewPageResponse;
import de.urr4.monsters.application.usecases.GenerateNewImageForExistingMonster;
import de.urr4.monsters.application.usecases.GenerateNewMonsterUsecase;
import de.urr4.monsters.application.usecases.RemoveImageFromMonsterUsecase;
import de.urr4.monsters.application.usecases.RemoveMonsterUsecase;
import de.urr4.monsters.domain.IntRange;
import de.urr4.monsters.domain.monster.Monster;
import de.urr4.monsters.domain.monster.MonsterFilter;
import de.urr4.monsters.domain.monster.MonsterRepository;
import de.urr4.monsters.domain.monster.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/monsters")
@CrossOrigin(origins = "http://localhost:4200")
public class MonsterController {

    private final GenerateNewMonsterUsecase generateNewMonsterUsecase;
    private final GenerateNewImageForExistingMonster generateNewImageForExistingMonster;
    private final RemoveImageFromMonsterUsecase removeImageFromMonsterUsecase;
    private final RemoveMonsterUsecase removeMonsterUsecase;
    private final MonsterRepository monsterRepository;

    public MonsterController(GenerateNewMonsterUsecase generateNewMonsterUsecase, GenerateNewImageForExistingMonster generateNewImageForExistingMonster, RemoveImageFromMonsterUsecase removeImageFromMonsterUsecase, RemoveMonsterUsecase removeMonsterUsecase, MonsterRepository monsterRepository) {
        this.generateNewMonsterUsecase = generateNewMonsterUsecase;
        this.generateNewImageForExistingMonster = generateNewImageForExistingMonster;
        this.removeImageFromMonsterUsecase = removeImageFromMonsterUsecase;
        this.removeMonsterUsecase = removeMonsterUsecase;
        this.monsterRepository = monsterRepository;
    }

    @GetMapping(path = "/filterParams", produces = MediaType.APPLICATION_JSON_VALUE)
    public FilterParamResponse getFilterParams() {
        List<String> types = monsterRepository.getAllTypes().stream().map(String::toLowerCase).toList();
        List<String> sizes = monsterRepository.getAllSizes().stream().map(Size::getValue).map(String::toLowerCase).toList();
        IntRange armorClassRange = monsterRepository.getCurrentArmorClassRange();
        IntRange hitpointRange = monsterRepository.getCurrentHitpointRange();
        return new FilterParamResponse(
                armorClassRange,
                hitpointRange,
                types,
                sizes);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public MonsterPreviewPageResponse getMonsterByFilter(
            @RequestParam(value = "nameIncludes", required = false, defaultValue = "") String nameIncludes,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "armorClassFrom", required = false) Integer armorClassFrom,
            @RequestParam(value = "armorClassTo", required = false) Integer armorClassTo,
            @RequestParam(value = "hitPointsFrom", required = false) Integer hitPointsFrom,
            @RequestParam(value = "hitPointsTo", required = false) Integer hitPointsTo,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize) {
        MonsterFilter monsterFilter = MonsterFilter.builder()
                .nameIncludes(nameIncludes)
                .size(size)
                .type(type)
                .armorClassRange(new IntRange(armorClassFrom, armorClassTo))
                .hitPointRange(new IntRange(hitPointsFrom, hitPointsTo))
                .pageNumber(page)
                .pageSize(pageSize)
                .build();
        monsterFilter.normalize();
        log.info("Filtering for {}", monsterFilter);
        List<MonsterPreview> monsterPreviews = monsterRepository.getMonstersByFilter(monsterFilter).stream().map(MonsterPreview::from).toList();
        int numberOfMatchingMonsters = monsterRepository.getNumberOfMatchingMonsters(monsterFilter);
        MonsterPreviewPageResponse monsterPreviewPageResponse = new MonsterPreviewPageResponse(monsterPreviews, numberOfMatchingMonsters);
        return monsterPreviewPageResponse;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Monster getMonsterById(@PathVariable("id") String id) {
        return monsterRepository.getMonsterById(UUID.fromString(id));
    }

    @Transactional
    @PutMapping(path = "/{id}/image")
    public UUID generateNewImageForMonster(@PathVariable("id") String id) {
        log.info("Generating new image for monster {}", id);
        return generateNewImageForExistingMonster.generateNewImageForExistingMonster(UUID.fromString(id));
    }

    @PostMapping(path = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MonsterPreview generateNewMonster(@RequestBody GenerateMonsterRequest generateMonsterRequest) {
        log.info("Got request {}", generateMonsterRequest);
        UUID monsterId = generateNewMonsterUsecase.generateMonster(generateMonsterRequest.getPrompt(), generateMonsterRequest.isGenerateImage());
        Monster monster = monsterRepository.getMonsterById(monsterId);
        log.info("Generated monster {}", monster.getName());
        return MonsterPreview.from(monster);
    }

    @DeleteMapping(path = "/{monsterId}")
    public void removeMonster(@PathVariable("monsterId") String monsterId) {
        removeMonsterUsecase.removeMonster(UUID.fromString(monsterId));
    }

    @Transactional
    @DeleteMapping(path = "/{monsterId}/image/{imageId}")
    public void removeImageFromMonster(
            @PathVariable("monsterId") String monsterId,
            @PathVariable("imageId") String imageId
    ) {
        log.info("Removing image {} from monster {}", imageId, monsterId);
        removeImageFromMonsterUsecase.removeImageFromMonster(UUID.fromString(monsterId), UUID.fromString(imageId));
    }


}

package de.urr4.pnp.adapter.web;

import de.urr4.pnp.adapter.web.requests.GenerateCreatureRequest;
import de.urr4.pnp.adapter.web.responses.CreatureFilterParamResponse;
import de.urr4.pnp.adapter.web.responses.CreaturePreview;
import de.urr4.pnp.adapter.web.responses.CreaturePreviewPageResponse;
import de.urr4.pnp.application.usecases.creature.GenerateNewImageForExistingCreatureUsecase;
import de.urr4.pnp.application.usecases.creature.GenerateNewCreatureUsecase;
import de.urr4.pnp.application.usecases.creature.RemoveImageFromCreatureUsecase;
import de.urr4.pnp.application.usecases.creature.RemoveCreatureUsecase;
import de.urr4.pnp.domain.IntRange;
import de.urr4.pnp.domain.creature.Creature;
import de.urr4.pnp.domain.creature.CreatureFilter;
import de.urr4.pnp.domain.creature.CreatureRepository;
import de.urr4.pnp.domain.creature.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/creatures")
@CrossOrigin(origins = "http://localhost:4200")
public class CreatureController {

    private final GenerateNewCreatureUsecase generateNewCreatureUsecase;
    private final GenerateNewImageForExistingCreatureUsecase generateNewImageForExistingCreatureUsecase;
    private final RemoveImageFromCreatureUsecase removeImageFromCreatureUsecase;
    private final RemoveCreatureUsecase removeCreatureUsecase;
    private final CreatureRepository creatureRepository;

    public CreatureController(GenerateNewCreatureUsecase generateNewCreatureUsecase, GenerateNewImageForExistingCreatureUsecase generateNewImageForExistingCreatureUsecase, RemoveImageFromCreatureUsecase removeImageFromCreatureUsecase, RemoveCreatureUsecase removeCreatureUsecase, CreatureRepository creatureRepository) {
        this.generateNewCreatureUsecase = generateNewCreatureUsecase;
        this.generateNewImageForExistingCreatureUsecase = generateNewImageForExistingCreatureUsecase;
        this.removeImageFromCreatureUsecase = removeImageFromCreatureUsecase;
        this.removeCreatureUsecase = removeCreatureUsecase;
        this.creatureRepository = creatureRepository;
    }

    @GetMapping(path = "/filterParams", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreatureFilterParamResponse getFilterParams() {
        List<String> types = creatureRepository.getAllTypes().stream().map(String::toLowerCase).toList();
        List<String> sizes = creatureRepository.getAllSizes().stream().map(Size::getValue).map(String::toLowerCase).toList();
        IntRange armorClassRange = creatureRepository.getCurrentArmorClassRange();
        IntRange hitpointRange = creatureRepository.getCurrentHitpointRange();
        return new CreatureFilterParamResponse(
                armorClassRange,
                hitpointRange,
                types,
                sizes);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CreaturePreviewPageResponse getCreatureByFilter(
            @RequestParam(value = "nameIncludes", required = false, defaultValue = "") String nameIncludes,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "armorClassFrom", required = false) Integer armorClassFrom,
            @RequestParam(value = "armorClassTo", required = false) Integer armorClassTo,
            @RequestParam(value = "hitPointsFrom", required = false) Integer hitPointsFrom,
            @RequestParam(value = "hitPointsTo", required = false) Integer hitPointsTo,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize) {
        CreatureFilter creatureFilter = CreatureFilter.builder()
                .nameIncludes(nameIncludes)
                .size(size)
                .type(type)
                .armorClassRange(new IntRange(armorClassFrom, armorClassTo))
                .hitPointRange(new IntRange(hitPointsFrom, hitPointsTo))
                .pageNumber(page)
                .pageSize(pageSize)
                .build();
        creatureFilter.normalize();
        log.info("Filtering for {}", creatureFilter);
        List<CreaturePreview> creaturePreviews = creatureRepository.getCreatureByFilter(creatureFilter).stream().map(CreaturePreview::from).toList();
        int numberOfMatchingCreatures = creatureRepository.getNumberOfMatchingCreatures(creatureFilter);
        CreaturePreviewPageResponse creaturePreviewPageResponse = new CreaturePreviewPageResponse(creaturePreviews, numberOfMatchingCreatures);
        return creaturePreviewPageResponse;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Creature getCreatureById(@PathVariable("id") String id) {
        return creatureRepository.getCreatureById(UUID.fromString(id));
    }

    @Transactional
    @PutMapping(path = "/{id}/image")
    public UUID generateNewImageForCreature(@PathVariable("id") String id) {
        log.info("Generating new image for creature {}", id);
        return generateNewImageForExistingCreatureUsecase.generateNewImageForExistingCreature(UUID.fromString(id));
    }

    @PostMapping(path = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreaturePreview generateNewCreature(@RequestBody GenerateCreatureRequest generateCreatureRequest) {
        log.info("Got request {}", generateCreatureRequest);
        UUID creatureId = generateNewCreatureUsecase.generateCreature(generateCreatureRequest.getPrompt(), generateCreatureRequest.isGenerateImage());
        Creature creature = creatureRepository.getCreatureById(creatureId);
        log.info("Generated creature {}", creature.getName());
        return CreaturePreview.from(creature);
    }

    @DeleteMapping(path = "/{creatureId}")
    public void removeCreature(@PathVariable("creatureId") String creatureId) {
        removeCreatureUsecase.removeCreature(UUID.fromString(creatureId));
    }

    @Transactional
    @DeleteMapping(path = "/{creatureId}/image/{imageId}")
    public void removeImageFromCreature(
            @PathVariable("creatureId") String creatureId,
            @PathVariable("imageId") String imageId
    ) {
        log.info("Removing image {} from creature {}", imageId, creatureId);
        removeImageFromCreatureUsecase.removeImageFromCreature(UUID.fromString(creatureId), UUID.fromString(imageId));
    }


}

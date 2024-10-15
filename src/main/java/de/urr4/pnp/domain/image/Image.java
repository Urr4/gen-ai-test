package de.urr4.pnp.domain.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Image {
    private UUID imageId;
    private byte[] imageBytes;
}

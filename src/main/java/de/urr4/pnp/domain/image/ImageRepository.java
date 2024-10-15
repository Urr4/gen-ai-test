package de.urr4.pnp.domain.image;

import java.util.UUID;

public interface ImageRepository {
    UUID saveImage(Image image);

    byte[] getImageById(UUID imageId);
}

package de.urr4.monsters.adapter.web.responses;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImageResponse {
    private UUID imageId;
    private byte[] imageBytes;
}

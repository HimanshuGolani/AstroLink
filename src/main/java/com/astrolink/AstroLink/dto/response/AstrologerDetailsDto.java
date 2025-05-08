package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AstrologerDetailsDto {
    private UUID astrologerId;
    private int astrologerRating;
}

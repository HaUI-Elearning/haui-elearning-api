package com.elearning.haui.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsDTO {
    private String name;
    private LocalDateTime joinDate;
}

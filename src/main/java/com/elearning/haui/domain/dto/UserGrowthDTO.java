package com.elearning.haui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGrowthDTO {
    private long newUsers;
    private long newTeachers;
    private int month;
    private int year;
}
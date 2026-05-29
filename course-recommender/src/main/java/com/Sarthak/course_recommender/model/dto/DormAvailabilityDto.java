package com.Sarthak.course_recommender.model.dto;

import com.Sarthak.course_recommender.model.Dorm;
import lombok.Getter;

@Getter
public class DormAvailabilityDto {
    private final Long id;
    private final String name;
    private final long occupancyCount;

    public DormAvailabilityDto(Dorm dorm, long occupancyCount) {
        this.id = dorm.getId();
        this.name = dorm.getName();
        this.occupancyCount = occupancyCount;
    }
}
package com.caco.sitedocaco.dto.response;

import com.caco.sitedocaco.entity.UserProfile;
import com.caco.sitedocaco.entity.enums.CourseType;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        CourseType course,
        String otherCourseName,
        int entryYear,
        LocalDateTime createdAt
) {
    public static UserProfileResponse fromEntity(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getCourse(),
                profile.getOtherCourseName(),
                profile.getEntryYear(),
                profile.getCreatedAt()
        );
    }
}


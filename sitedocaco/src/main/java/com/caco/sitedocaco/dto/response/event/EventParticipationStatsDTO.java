package com.caco.sitedocaco.dto.response.event;

public record EventParticipationStatsDTO(
        long interestedCount,
        long goingCount,
        long notGoingCount,
        long totalParticipants // interested + going
) {}
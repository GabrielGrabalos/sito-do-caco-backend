package com.caco.sitedocaco.dto.request.event;

import com.caco.sitedocaco.entity.event.UserEvent;

public record SaveEventRequestDTO(
        UserEvent.ParticipationStatus status
) {}
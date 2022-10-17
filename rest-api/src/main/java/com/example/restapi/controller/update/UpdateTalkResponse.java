package com.example.restapi.controller.update;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
class UpdateTalkResponse {
    private final String id;
    private final String title;
    private final String author;
    private final LocalDateTime createDate;
    private final String scheduleDate;
    private final long viewCount;
    private final long likeCount;
    private final String link;
}

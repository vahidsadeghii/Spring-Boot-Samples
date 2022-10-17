package com.example.restapi.controller.create;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
class CreateNewTalkResponse {
    private final String id;
    private final String title;
    private final String author;
    private final LocalDateTime createDate;
    private final String scheduleDate;
    private final long viewCount;
    private final long likeCount;
    private final String link;
}

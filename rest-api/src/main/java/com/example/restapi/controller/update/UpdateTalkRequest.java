package com.example.restapi.controller.update;

import lombok.Getter;

@Getter
class UpdateTalkRequest {
    private String title;
    private String author;
    private String scheduleDate;
    private String link;
    private Long viewCount;
    private Long likeCount;
}

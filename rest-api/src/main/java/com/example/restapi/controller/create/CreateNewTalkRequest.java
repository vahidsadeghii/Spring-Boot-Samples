package com.example.restapi.controller.create;

import lombok.Getter;

@Getter
class CreateNewTalkRequest {
    private String title;
    private String author;
    private String scheduleDate;
    private String link;
}

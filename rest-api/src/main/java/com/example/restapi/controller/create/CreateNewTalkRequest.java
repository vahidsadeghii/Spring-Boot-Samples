package com.example.restapi.controller.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewTalkRequest {
    private String title;
    private String author;
    private String scheduleDate;
    private String link;
}

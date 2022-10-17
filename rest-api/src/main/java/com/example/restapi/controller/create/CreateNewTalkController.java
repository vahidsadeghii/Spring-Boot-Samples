package com.example.restapi.controller.create;

import com.example.restapi.domain.Talk;
import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Secured({"ROLE_ADMIN"})
public class CreateNewTalkController {
    private final TalkService talkService;

    @PostMapping("${apis.secure.prefix}/talks")
    public CreateNewTalkResponse handle(@RequestBody CreateNewTalkRequest request) {
        Talk newTalk = talkService.createNewTalk(request.getTitle(), request.getAuthor(), request.getScheduleDate(), request.getLink());
        return CreateNewTalkResponse.builder()
                .id(newTalk.getId() + "")
                .title(newTalk.getTitle())
                .author(newTalk.getAuthor())
                .createDate(newTalk.getCreateDate())
                .scheduleDate(newTalk.getScheduleDate())
                .viewCount(newTalk.getViewCount())
                .likeCount(newTalk.getLikeCount())
                .link(newTalk.getLink())
                .build();
    }
}

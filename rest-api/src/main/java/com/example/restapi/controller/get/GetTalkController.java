package com.example.restapi.controller.get;

import com.example.restapi.domain.Talk;
import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Secured({"ROLE_ADMIN"})
public class GetTalkController {
    private final TalkService talkService;

    @GetMapping("${apis.secure.prefix}/talks/{id}")
    public GetTalkResponse handle(@PathVariable("id") int id) {
        Talk talk = talkService.findTalk(id);
        return GetTalkResponse.builder()
                .id(talk.getId() + "")
                .title(talk.getTitle())
                .author(talk.getAuthor())
                .createDate(talk.getCreateDate())
                .scheduleDate(talk.getScheduleDate())
                .viewCount(talk.getViewCount())
                .likeCount(talk.getLikeCount())
                .link(talk.getLink())
                .build();
    }

}

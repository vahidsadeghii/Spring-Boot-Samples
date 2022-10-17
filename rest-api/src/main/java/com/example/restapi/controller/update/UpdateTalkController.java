package com.example.restapi.controller.update;

import com.example.restapi.domain.Talk;
import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Secured({"ROLE_ADMIN"})
public class UpdateTalkController {
    private final TalkService talkService;

    @PutMapping("${apis.secure.prefix}/talks/{id}")
    public UpdateTalkResponse handle(@PathVariable("id") int id,
                                     @RequestBody UpdateTalkRequest request) {
        Talk newTalk = talkService.updateTalk(
                id, request.getTitle(), request.getAuthor(), request.getScheduleDate(),
                request.getViewCount(), request.getLikeCount(), request.getLink());

        return UpdateTalkResponse.builder()
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

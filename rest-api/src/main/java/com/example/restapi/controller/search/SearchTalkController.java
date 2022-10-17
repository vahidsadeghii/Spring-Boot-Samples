package com.example.restapi.controller.search;

import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Secured({"ROLE_ADMIN"})
public class SearchTalkController {
    private final TalkService talkService;

    @GetMapping("${apis.secure.prefix}/talks")
    public List<SearchTalkResponse> handle(@RequestParam(value = "title", required = false, defaultValue = "") String title,
                                           @RequestParam(value = "author", required = false, defaultValue = "") String author,
                                           @RequestParam(value = "views", required = false, defaultValue = "") Long views,
                                           @RequestParam(value = "likes", required = false, defaultValue = "") Long likes,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        return talkService.findTalks(title, author, views, likes, pageSize, pageIndex).stream().map(
                talk -> SearchTalkResponse.builder()
                        .id(talk.getId() + "")
                        .title(talk.getTitle())
                        .author(talk.getAuthor())
                        .createDate(talk.getCreateDate())
                        .scheduleDate(talk.getScheduleDate())
                        .viewCount(talk.getViewCount())
                        .likeCount(talk.getLikeCount())
                        .link(talk.getLink())
                        .build()
        ).collect(Collectors.toList());
    }

}

package com.example.restapi.controller.delete;

import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Secured({"ROLE_ADMIN"})
public class DeleteTalkController {
    private final TalkService talkService;

    @DeleteMapping("${apis.secure.prefix}/talks/{id}")
    public void handle(@PathVariable(value = "id") int id) {
        talkService.deleteTalk(id);
    }
}

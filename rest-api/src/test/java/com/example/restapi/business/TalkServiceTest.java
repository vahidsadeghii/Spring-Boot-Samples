package com.example.restapi.business;

import com.example.restapi.domain.Talk;
import com.example.restapi.exception.TalkAlreadyExistsException;
import com.example.restapi.repository.TalkRepository;
import com.example.restapi.service.TalkService;
import com.example.restapi.service.impl.TalkServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {TalkRepository.class, TalkServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class TalkServiceTest {
    @Autowired
    TalkService talkService;
    @MockBean
    TalkRepository talkRepository;

    private Talk talk;

    @BeforeEach
    public void initMocks() {
        talk = Talk.builder()
                .id(1)
                .title("TedTalk")
                .author("TedTalk Author")
                .scheduleDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM yyyy")))
                .viewCount(1)
                .likeCount(1)
                .link("ted.com")
                .build();

        when(talkRepository.save(any())).thenReturn(talk);
        when(talkRepository.findById(talk.getId())).thenReturn(Optional.of(talk));
        when(talkRepository.findFirstByTitleAndAuthor(talk.getTitle(), talk.getAuthor())).thenReturn(Optional.of(talk));
    }

    @Test
    public void successfullyLoadTalk() {
        Talk talk = talkService.loadTalk(this.talk.getTitle(),
                this.talk.getAuthor(),
                this.talk.getScheduleDate(), this.talk.getViewCount(), this.talk.getLikeCount(),
                this.talk.getLink());

        verify(talkRepository, times(1)).save(any());

        Assertions.assertNotNull(talk);
    }

    @Test
    public void successfullyCreateNewTalk() {
        Talk talk = talkService.createNewTalk(RandomStringUtils.random(10),
                RandomStringUtils.random(20), LocalDate.now().format(DateTimeFormatter.ofPattern("MMM yyyy")),
                "ted.com");

        verify(talkRepository, times(1)).save(any());
        verify(talkRepository, times(1)).findFirstByTitleAndAuthor(any(), any());

        Assertions.assertNotNull(talk);
    }

    @Test
    public void throwTalkAlreadyExistExceptionOnDuplicatedSaveTalk() {
        TalkAlreadyExistsException talkAlreadyExistsException = Assertions.assertThrows(TalkAlreadyExistsException.class, () -> talkService.createNewTalk(
                talk.getTitle(), talk.getAuthor(), talk.getScheduleDate(), talk.getLink()
        ));

        verify(talkRepository, times(1)).findFirstByTitleAndAuthor(any(), any());
        verify(talkRepository, times(0)).save(any());

        Assertions.assertNotNull(talkAlreadyExistsException);
    }
}

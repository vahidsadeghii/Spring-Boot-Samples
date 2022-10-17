package com.example.restapi.service.impl;

import com.example.restapi.domain.Talk;
import com.example.restapi.exception.TalkAlreadyExistsException;
import com.example.restapi.exception.TalkNotFoundException;
import com.example.restapi.repository.TalkRepository;
import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkServiceImpl implements TalkService {
    private final TalkRepository talkRepository;

    @Override
    public Talk loadTalk(String title, String author, String scheduleDate, long viewCount, long likeCount, String link) {
        return talkRepository.save(
                Talk.builder()
                        .createDate(LocalDateTime.now())
                        .title(title)
                        .author(author)
                        .scheduleDate(scheduleDate)
                        .viewCount(viewCount)
                        .likeCount(likeCount)
                        .link(link)
                        .build()
        );
    }

    @Override
    public Talk createNewTalk(String title, String author, String scheduleDate, String link) {
        talkRepository.findFirstByTitleAndAuthor(title, author).ifPresent(
                talk -> {
                    throw new TalkAlreadyExistsException();
                }
        );

        return loadTalk(title, author, scheduleDate, 0, 0, link);
    }

    @Override
    public Talk updateTalk(int id, String title, String author, String scheduleDate, Long viewCount, Long likeCount, String link) {
        Talk talk = talkRepository.findById(id).orElseThrow(TalkNotFoundException::new);
        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author)) {
            if (!talk.getTitle().equals(title) || !talk.getAuthor().equals(author)) {
                talkRepository.findFirstByTitleAndAuthor(title, author).ifPresent(
                        foundTalk -> {
                            if (foundTalk.getId() != id) {
                                throw new TalkAlreadyExistsException();
                            }
                        }
                );
            }
        }

        if (StringUtils.isNotBlank(title)) {
            talk.setTitle(title);
        }

        if (StringUtils.isNotBlank(author)) {
            talk.setAuthor(author);
        }

        if (StringUtils.isNotBlank(scheduleDate)) {
            talk.setScheduleDate(talk.getScheduleDate());
        }

        if (viewCount != null) {
            talk.setViewCount(viewCount);
        }

        if (likeCount != null) {
            talk.setLikeCount(likeCount);
        }

        if (StringUtils.isNotBlank(link)) {
            talk.setLink(link);
        }

        talkRepository.save(talk);

        return talk;
    }

    @Override
    public void deleteTalk(int id) {
        Talk talk = talkRepository.findById(id).orElseThrow(TalkNotFoundException::new);
        talkRepository.delete(talk);
    }

    @Override
    public List<Talk> findTalks(String title, String author, Long viewCount, Long likeCount, int pageSize, int pageIndex) {
        return talkRepository.findAllByTitleOrAuthorOrViewCountOrLikeCountOrderByCreateDateDesc(
                title, author, viewCount, likeCount,
                PageRequest.of(pageIndex, pageSize, Sort.by("createDate").descending()));
    }

    @Override
    public Talk findTalk(int id) {
        return talkRepository.findById(id).orElseThrow(TalkNotFoundException::new);
    }
}

package com.example.restapi.service;

import com.example.restapi.domain.Talk;

import java.util.List;

public interface TalkService {
    Talk loadTalk(String title, String author, String scheduleDate, long viewCount, long likeCount, String link);

    Talk createNewTalk(String title, String author, String scheduleDate, String link);

    Talk updateTalk(int id, String title, String author, String scheduleDate, Long viewCount, Long likeCount, String link);

    void deleteTalk(int id);

    List<Talk> findTalks(String title, String author, Long viewCount, Long likeCount, int pageSize, int pageIndex);

    Talk findTalk(int id);
}

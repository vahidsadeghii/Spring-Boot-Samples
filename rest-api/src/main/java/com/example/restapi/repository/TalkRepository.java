package com.example.restapi.repository;

import com.example.restapi.domain.Talk;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TalkRepository extends JpaRepository<Talk, Integer> {
    Optional<Talk> findFirstByTitleAndAuthor(String title, String author);


    @Query(value = "from Talk a where (:title is null or a.title like %:title%) and " +
            "(:author is null or a.author like %:author%) and " +
            "(:viewCount is null or a.viewCount = :viewCount) and " +
            "(:likeCount is null or a.likeCount = :likeCount)")
    List<Talk> findAllByTitleOrAuthorOrViewCountOrLikeCountOrderByCreateDateDesc(@Param("title") String title,
                                                                                 @Param("author") String author,
                                                                                 @Param("viewCount") Long viewCount,
                                                                                 @Param("likeCount") Long likeCount,
                                                                                 Pageable pageable);
}

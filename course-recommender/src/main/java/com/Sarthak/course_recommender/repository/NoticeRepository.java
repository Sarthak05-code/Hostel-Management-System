package com.Sarthak.course_recommender.repository;

import com.Sarthak.course_recommender.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByPostedAtDesc();
}
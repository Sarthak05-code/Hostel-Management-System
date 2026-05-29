package com.Sarthak.course_recommender.service;

import com.Sarthak.course_recommender.model.Notice;
import com.Sarthak.course_recommender.repository.NoticeRepository;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByPostedAtDesc();
    }

    @Transactional(readOnly = true)
    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with id: " + id));
    }

    @Transactional
    public Notice createNotice(Notice notice) {
        notice.setId(null);
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = getNoticeById(id);
        noticeRepository.delete(notice);
    }
}
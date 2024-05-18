package com.kukharev.pastbin.service;

import com.kukharev.pastbin.repository.TextBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TextBlockCleanupTask {

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Scheduled(fixedRate = 60000) // Запуск каждые 60 секунд
    public void cleanUpExpiredTextBlocks() {
        textBlockRepository.deleteAllByExpiryTimeBefore(LocalDateTime.now());
    }
}

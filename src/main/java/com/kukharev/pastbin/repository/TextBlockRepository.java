package com.kukharev.pastbin.repository;

import com.kukharev.pastbin.model.TextBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TextBlockRepository extends JpaRepository<TextBlock, Long> {
    Optional<TextBlock> findByHash(String hash);

    void deleteAllByExpiryTimeBefore(LocalDateTime now);
}

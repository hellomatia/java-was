package codesquad.domain.article.model;

import java.time.LocalDateTime;

public record Comment(
        Long id,
        Long postId,
        String userId,
        String userName,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

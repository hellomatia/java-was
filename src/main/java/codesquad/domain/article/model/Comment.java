package codesquad.domain.article.model;

import java.time.LocalDateTime;

public record Comment(
        String id,
        String postId,
        String userId,
        String userName,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

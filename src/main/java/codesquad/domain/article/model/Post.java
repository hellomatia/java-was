package codesquad.domain.article.model;

import java.time.LocalDateTime;

public record Post(
        Long id,
        String title,
        String userId,
        String userName,
        String imageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public Post withUpdatedTimestamp() {
        return new Post(id, title, userId, userName, imageUrl, content, createdAt, LocalDateTime.now());
    }
}

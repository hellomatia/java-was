package codesquad.domain.article.model;

import java.time.LocalDateTime;

public record Post(
        Long id,
        String accountId,
        String accountNickname,
        String imageUrl,
        String content,
        int likesCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public Post withUpdatedTimestamp() {
        return new Post(id, accountId, accountNickname, imageUrl, content, likesCount, createdAt, LocalDateTime.now());
    }

    public Post withIncrementedLikes() {
        return new Post(id, accountId, accountNickname, imageUrl, content, likesCount + 1, createdAt, LocalDateTime.now());
    }
}

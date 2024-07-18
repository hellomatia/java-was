package codesquad.domain.article.model;

import java.util.List;

public record Post(
        String id,
        String title,
        String userId,
        String userName,
        String imageUrl,
        String content,
        List<Comment> comments
) {
    public Post addComments(List<Comment> comments) {
        return new Post(id, title, userId, userName, imageUrl, content, comments);
    }
}

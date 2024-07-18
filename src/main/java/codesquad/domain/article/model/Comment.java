package codesquad.domain.article.model;

public record Comment(
        String id,
        String postId,
        String userId,
        String userName,
        String comment
) {
}

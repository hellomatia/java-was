package codesquad.domain.user;

import codesquad.database.DataBase;
import codesquad.domain.user.model.User;
import codesquad.server.core.Server;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.MultipartFormDataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;
import static codesquad.domain.util.Constants.HTTP_METHOD_POST;
import static codesquad.server.util.FileUtils.readFileContent;
import static codesquad.server.util.FileUtils.saveImage;

@Handler("/user/create")
public class UserJoinHandler extends CustomRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserJoinHandler.class);

    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse moveJoin(HttpRequest request) {
        return ok(readFileContent("/static/registration/index.html"))
                .build();
    }

    @HttpMethod(HTTP_METHOD_POST)
    public HttpResponse processJoin(HttpRequest request) throws IOException {
        MultipartFormDataParser.ParsedData parsedData = MultipartFormDataParser.parse(request);
        String name = parsedData.getFormData().get("name");
        String email = parsedData.getFormData().get("email");
        String password = parsedData.getFormData().get("password");
        String userId = parsedData.getFormData().get("userId");
        String userImageUrl = saveImage(parsedData.getFileData().get("userImage"));
        User user = new User(name, password, userId, email, userImageUrl);
        DataBase.addUser(user);
        logger.debug("User info: {}", user);
        return redirect("/").build();
    }
}

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

import static codesquad.server.util.FileUtils.saveImage;

@Handler("/user/create")
public class UserJoinHandler extends CustomRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public UserJoinHandler() {
    }

    @HttpMethod("POST")
    public HttpResponse createUser(HttpRequest request) throws IOException {
        MultipartFormDataParser.ParsedData parsedData = MultipartFormDataParser.parse(request);

        String name = parsedData.getFormData().get("name");
        String email = parsedData.getFormData().get("email");
        String password = parsedData.getFormData().get("password");
        String userId = parsedData.getFormData().get("userId");
        String userImageUrl = saveImage(parsedData.getFileData().get("userImage"));
        User user = new User(name, password, userId, email, userImageUrl);
        DataBase.addUser(user);
        logger.debug("User info: {}", user);
        return redirect("/index.html").build();
    }
}

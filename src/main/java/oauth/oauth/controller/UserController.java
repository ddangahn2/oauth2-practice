package oauth.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class UserController {

    @GetMapping("/success")
    public String success() {
        return "로그인 성공!";
    }

    @GetMapping("/failure")
    public String failure() {
        return "로그인 실패!";
    }
}

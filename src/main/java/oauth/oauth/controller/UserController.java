package oauth.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oauth.oauth.domain.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

@RestController
@RequestMapping("/oauth2")
public class UserController {

    @GetMapping("/success")
    public void success(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if(request.getAttribute(ERROR_EXCEPTION) instanceof Exception){
            System.out.println("true = ");
        }
        else {
            System.out.println("false = ");
        }

        System.out.println("StatusCode.CREATED.getClass() = " + StatusCode.CREATED.getClass());
        response.sendError(2048, "Hello");
//        return ResponseEntity();
    }

    @GetMapping("/failure")
    public String failure() {
        return "로그인 실패!";
    }

}

package com.tg.jackysdailychallenge;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class JackysdailychallengeController {

    @RequestMapping("/")
    public String getAppStatus() {
        return "App is running!";
    }
}

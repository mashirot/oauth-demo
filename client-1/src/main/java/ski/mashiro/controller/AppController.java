package ski.mashiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.service.AppService;

@RestController
@RequestMapping("/")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping
    public String getPublicData() {
        return "Public data";
    }

    @GetMapping("/private-data")
    public String getPrivateData() {
        String jwtToken = appService.getJwtToken();
        return jwtToken;
    }
}

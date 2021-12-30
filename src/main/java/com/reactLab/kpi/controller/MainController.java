package com.reactLab.kpi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @GetMapping(value = "/userinfo/{user}")
    public String userinfo(@PathVariable("user") String user, Model model){
        model.addAttribute("user", user);
        return "userStat";
    }

    @GetMapping(value = "/recent/{user}")
    public String recentUser(@PathVariable("user") String user, Model model){
        model.addAttribute("user", user);
        return "userRecent";
    }

    @GetMapping(value = "/recent")
    public String recent(){
        return "recent";
    }

    @GetMapping(value = "/mostActive/{days}")
    public String recentUser(@PathVariable("days") int days, Model model){
        model.addAttribute("days", days);
        return "mostActive";
    }
}

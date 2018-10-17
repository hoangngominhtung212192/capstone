package com.tks.gwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public interface WebController {

    /**
     *
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView loginPage();
}

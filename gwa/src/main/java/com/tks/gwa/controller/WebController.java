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

    @RequestMapping(value = "/register")
    public ModelAndView registerPage();

    @RequestMapping(value = "/")
    public ModelAndView indexPage();

    @RequestMapping(value = "/model/")
    public ModelAndView modelPage();

    @RequestMapping(value = "/trade-market/post-new-trade")
    public ModelAndView postNewTradePage();

    @RequestMapping(value = "/model/modeldetail")
    public ModelAndView modelDetailPage();

    @RequestMapping(value = "/user/profile")
    public ModelAndView profilePage();

    @RequestMapping(value = "/admin/user/search")
    public ModelAndView searchAccountPage();

    @RequestMapping(value = "/admin/model/create")
    public ModelAndView createModelPage();

    @RequestMapping(value = "/admin/model/edit")
    public ModelAndView editModelPage();
}

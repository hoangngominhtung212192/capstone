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

    @RequestMapping(value = "/model/modeldetail")
    public ModelAndView modelDetailPage();

    @RequestMapping(value = "/user/profile")
    public ModelAndView profilePage();

    @RequestMapping(value = "/create-Article")
    public ModelAndView createArticlePage();

    @RequestMapping(value = "/admin/user/search")
    public ModelAndView searchAccountPage();

    @RequestMapping(value = "/admin/model/create")
    public ModelAndView createModelPage();

    @RequestMapping(value = "/admin/model/edit")
    public ModelAndView editModelPage();

    @RequestMapping(value = {"/trade-market","/trade-market/trading"})
    public ModelAndView tradepostListingPage();

    @RequestMapping(value = "/trade-market/my-trade")
    public ModelAndView myTradePage();

    @RequestMapping(value = "/trade-market/my-order")
    public ModelAndView myOrderPage();

    @RequestMapping(value = "/trade-market/view-trade")
    public ModelAndView viewTradePage();

    @RequestMapping(value ={"trade-market/post-new-trade","trade-market/edit-trade"})
    public ModelAndView addEditTradePage();
}

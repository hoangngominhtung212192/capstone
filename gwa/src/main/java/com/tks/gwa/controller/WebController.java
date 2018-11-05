package com.tks.gwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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

    @RequestMapping(value = "/model/detail")
    public ModelAndView modelDetailPage();

    @RequestMapping(value = "/user/profile")
    public ModelAndView profilePage();

    @RequestMapping(value = "/article/create-article")
    public ModelAndView createArticlePage();

    @RequestMapping(value = "/article")
    public ModelAndView searchArticlePage();

    @RequestMapping(value = "/article/detail")
    public ModelAndView viewArticlePage();

    @RequestMapping(value = "/article/edit")
    public ModelAndView editArticlePage();

    @RequestMapping(value = "/admin/user/search")
    public ModelAndView searchAccountPage();

    @RequestMapping(value = "/admin/model/create")
    public ModelAndView createModelPage(HttpSession session);

    @RequestMapping(value = "/admin/model/edit")
    public ModelAndView editModelPage();


    @RequestMapping(value = "/admin/model/crawl")
    public ModelAndView crawlModelPage();

    @RequestMapping(value = "/admin/model/pending")
    public ModelAndView pendingModelPage();

    @RequestMapping(value = "/403")
    public ModelAndView forbiddenPage();

    @RequestMapping(value = "/admin/article")
    public ModelAndView searchArticleAdminPage();

    @RequestMapping(value = "/admin/article/create")
    public ModelAndView createArticleAdminPage();

    @RequestMapping(value = "/admin/article/edit")
    public ModelAndView editArticleAdminPage();

    @RequestMapping(value = "/admin/article/detail")
    public ModelAndView viewArticleAdminPage();

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

    @RequestMapping(value = "/event")
    public ModelAndView viewAllEventPage();

    @RequestMapping(value = "/event/detail")
    public ModelAndView viewEventDetailPage();

    @RequestMapping(value = "/admin/event")
    public ModelAndView viewAllEventAdminPage();

    @RequestMapping(value = "/admin/event/edit")
    public ModelAndView editEventAdminPage();

    @RequestMapping(value = "/admin/event/create")
    public ModelAndView createEventAdminPage();

}

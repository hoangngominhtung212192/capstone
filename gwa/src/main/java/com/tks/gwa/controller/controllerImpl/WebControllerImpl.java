package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebControllerImpl implements WebController {

    /**
     *
     * @return
     */
    @Override
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @Override
    public ModelAndView registerPage() {
        return new ModelAndView("register");
    }

    /**
     *
     * @return
     */
    @Override
    public ModelAndView indexPage() {
        return new ModelAndView("index");
    }

    /**
     *
     * @return
     */
    @Override
    public ModelAndView modelPage() {
        return new ModelAndView("model");
    }


    /**
     *
     * @return
     */
    @Override
    public ModelAndView modelDetailPage() {
        return new ModelAndView("modeldetail");

    }

    @Override
    public ModelAndView profilePage() {
        return new ModelAndView("profile");
    }

    @Override
    public ModelAndView createArticlePage() {
        return new ModelAndView("create-article");
    }

    @Override
    public ModelAndView searchAccountPage() {
        return new ModelAndView("searchaccount");
    }

    @Override
    public ModelAndView createModelPage() {
        return new ModelAndView("createmodel");
    }

    @Override
    public ModelAndView editModelPage() {
        return new ModelAndView("editmodel");
    }

    @Override

    public ModelAndView searchArticlePage() {
        return new ModelAndView("search-article");
    }

    @Override
    public ModelAndView viewArticlePage() {
        return new ModelAndView("article-detail");
    }

    @Override
    public ModelAndView editArticlePage() {
        return new ModelAndView("article-edit");
    }

    public ModelAndView tradepostListingPage() {
        return new ModelAndView("trade-listing");
    }

    @Override
    public ModelAndView myTradePage() {
        return new ModelAndView("my-trade");
    }

    @Override
    public ModelAndView myOrderPage() {
        return new ModelAndView("my-order");
    }

    @Override
    public ModelAndView viewTradePage() {
        return new ModelAndView("trade-details");
    }

    @Override
    public ModelAndView addEditTradePage() {
        return new ModelAndView("post-trade");
    }
}

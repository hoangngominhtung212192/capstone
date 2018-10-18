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
}

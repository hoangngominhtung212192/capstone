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
}

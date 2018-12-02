package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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
        return new ModelAndView("model");
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
        return new ModelAndView("search-account");
    }

    @Override
    public ModelAndView createModelPage(HttpSession session) {

        String role = (String) session.getAttribute("ROLE");
        if (role == null) {
            return new ModelAndView("access-denied");
        } else {
            System.out.println(role);
            if (!role.equalsIgnoreCase("ADMIN")) {
                return new ModelAndView("access-denied");
            }
        }

        return new ModelAndView("create-model");
    }

    @Override
    public ModelAndView searchArticleAdminPage() {
        return new ModelAndView("admin-article-search");
    }

    @Override
    public ModelAndView createArticleAdminPage() {
        return new ModelAndView("admin-article-create");
    }

    @Override
    public ModelAndView editArticleAdminPage() {
        return new ModelAndView("admin-article-edit");
    }

    @Override
    public ModelAndView viewArticleAdminPage() {
        return new ModelAndView("admin-article-detail");
    }

    @Override
    public ModelAndView editModelPage() {
        return new ModelAndView("edit-model");
    }

    @Override
    public ModelAndView crawlModelPage() {
        return new ModelAndView("crawl-model");
    }

    @Override
    public ModelAndView pendingModelPage() {
        return new ModelAndView("pending-model");
    }

    @Override
    public ModelAndView forbiddenPage() {
        return new ModelAndView("access-denied");
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

    @Override
    public ModelAndView viewAllEventPage() {
        return new ModelAndView("event-search");
    }

    @Override
    public ModelAndView viewEventDetailPage() {
        return new ModelAndView("event-detail");
    }

    @Override
    public ModelAndView viewAllEventAdminPage() {
        return new ModelAndView("admin-event-search");
    }

    @Override
    public ModelAndView editEventAdminPage() {
        return new ModelAndView("admin-event-edit");
    }

    @Override
    public ModelAndView createEventAdminPage() { return new ModelAndView("admin-event-create");
    }

    @Override
    public ModelAndView proposalListAdminPage() { return new ModelAndView("admin-proposal"); }

    @Override
    public ModelAndView proposalDetailAdminPage() { return new ModelAndView("admin-proposal-detail"); }

    @Override
    public ModelAndView createProposalPage() { return new ModelAndView("create-proposal"); }

    @Override
    public ModelAndView proposalDetailPage() {
        return new ModelAndView("proposal-detail");
    }

    public ModelAndView manageSchedule() {
        return new ModelAndView("admin-manage-schedule");
    }

    @Override
    public ModelAndView crawlArticlePage() {
        return new ModelAndView("crawl-article");
    }

    @Override
    public ModelAndView pendingArticlePage() {
        return new ModelAndView("pending-article");
    }

    @Override
    public ModelAndView manageModelPage() {
        return new ModelAndView("admin-manage-model");
    }

    @Override
    public ModelAndView forgotPasswordPage() {
        return new ModelAndView("forgot-password");
    }

    @Override
    public ModelAndView pendingTradepostPage() {
        return new ModelAndView("manipulate-tradepost");
    }
}

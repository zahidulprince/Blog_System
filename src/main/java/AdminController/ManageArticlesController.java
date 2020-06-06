package AdminController;

import io.javalin.http.Context;

public class ManageArticlesController {
    public static void getManageArticles(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/Article/manageArticles.html.pebble");
    }
}

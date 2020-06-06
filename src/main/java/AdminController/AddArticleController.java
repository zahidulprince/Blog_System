package AdminController;

import io.javalin.http.Context;

public class AddArticleController {
    public static void getAddArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/Article/addArticle.html.pebble");
    }
}

package AdminController;

import io.javalin.http.Context;

public class ManageCategoriesController {
    public static void getManageCategories(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/Category/manageCategories.html.pebble");
    }
}

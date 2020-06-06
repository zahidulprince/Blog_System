package AdminController;

import io.javalin.http.Context;

public class AddCategoryController {
    public static void getAddCategory(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/Category/addCategory.html.pebble");
    }
}

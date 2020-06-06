package AdminController;

import io.javalin.http.Context;

public class ManageUserController {
    public static void getManageUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/User/manageUsers.html.pebble");
    }
}

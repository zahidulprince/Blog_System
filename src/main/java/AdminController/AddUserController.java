package AdminController;

import io.javalin.http.Context;

public class AddUserController {
    public static void getAddUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());
        ctx.render("templates/admin/User/addUser.html.pebble");
    }
}

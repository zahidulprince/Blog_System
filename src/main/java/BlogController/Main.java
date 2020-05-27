package BlogController;

import io.javalin.Javalin;
import io.javalin.*;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.core.security.SecurityUtil.roles;


public class Main {

    static String domain = "http://localhost:7000";
    static DatabaseController dbController;

//    enum MyRoles implements Role{
//        Role_One, Role_Two;
//    }
//
//    static Role getUserRole(@NotNull Context ctx) {
//        String userrole = ctx.queryParam("role");
//        System.out.println(userrole);
//        return MyRoles.valueOf(userrole);
//    }
//
//    .accessManager((handler, ctx, permittedRoles) -> {
//        if (permittedRoles.contains(getUserRole(ctx))) {
//            handler.handle(ctx);
//        } else {
//            ctx.status(401).result("Unauthorized");
//        }
//    })

    public static void main(String[] args) {

        Javalin app = Javalin
                .create(javalinConfig -> {
                    javalinConfig.addStaticFiles("/public");
                }).start(7000);

        dbController = new DatabaseController();

        app.routes(() -> {

            before(AdminController.ensureLoginBeforeViewingEditor);
            get("/addData", ctx -> dbController.addData());
            get("/blog/:pn", BlogController::getBlogHome);
            get("/categories/:pn", CategoriesController::getCategories);
            get("/category/:ctgn/:pn", SingleCategoryController::getDesiredCategory);
            get("/article/:pn", ArticleController::getAricle);
            get("/login", AdminController.serveLoginPage);
            get("/check", ctx -> ctx.render("templates/adminHome.html.pebble"));
            get("/logout", AdminController.handleLogoutPost);
            post("/subscribed", SubscriberController::addSubscriber);
            post("/admin", AdminController.handleLoginPost);
        });
    }
}
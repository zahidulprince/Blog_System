package BlogController;

import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Context;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.core.security.SecurityUtil.roles;


public class Main {

    static String domain = "http://localhost:7000";
    static DatabaseController dbController;

    enum MyRoles implements Role{
        ANY_ONE, ADMIN, WRITER;
    }

    public static Set<Role> roleSet= new HashSet<Role>();

    List<Role> prince = Arrays.asList(MyRoles.WRITER, MyRoles.ADMIN);
    List<Role> anyOne = Arrays.asList(MyRoles.ANY_ONE);
    List<Role> writer = Arrays.asList(MyRoles.WRITER);

    List<String> princeCreds = List.of("Zahidul Islam Prince", "checkpass");
    List<String> writerCreds = List.of("P2", "Q2");

    Map<List<Role>, List<String>> map = Map.of(prince, princeCreds, writer, writerCreds);

    static boolean getUserRole(Context ctx, Set<Role> roleSet) {
        return true;
    }

    public static void main(String[] args) {

        roleSet.add(MyRoles.WRITER);
        roleSet.add(MyRoles.ADMIN);
        roleSet.add(MyRoles.ANY_ONE);

        Javalin app = Javalin
                .create(javalinConfig -> {
                    javalinConfig
                            .addStaticFiles("/public");
//                            .accessManager((handler, ctx, permittedRoles) -> {
//                                if (ctx.path().startsWith("/blog")) {
//                                    handler.handle(ctx);
//                                } else {
//                                    String currentUser = ctx.sessionAttribute("currentUser");
//                                    System.out.println(currentUser);
//                                    if (currentUser == null) {
//                                        AdminController.handleLoginPost(ctx);
//                                    }else if (getUserRole(ctx, roleSet)) {
//                                        handler.handle(ctx);
//                                    }
//
////                                MyRoles userRole = (MyRoles) getUserRole(ctx, roleSet);
//
////                                if (permittedRoles.contains(userRole)) {
////                                    handler.handle(ctx);
////                                }
//                                    else {
//                                        ctx.status(401).result("Unauthorized");
//                                    }
//                                }
//
//                            });
                }).start(7000);

        dbController = new DatabaseController();

        app.routes(() -> {

            before(AdminController.ensureLoginBeforeViewingEditor);

            get("/login", AdminController::serveLoginPage);
            get("/admin", ctx -> ctx.render("templates/adminHome.html.pebble"));
            get("/logout", AdminController.handleLogoutPost);
            post("/wronginfo", AdminController::handleLoginPost);

            path("/blog", () -> {
                get("/addData", ctx -> dbController.addData());
                get("/:pn", BlogController::getBlogHome);
                get("/categories/:pn", CategoriesController::getCategories);
                get("/category/:ctgn/:pn", SingleCategoryController::getDesiredCategory);
                get("/article/:pn", ArticleController::getAricle);
                post("/subscribed", SubscriberController::addSubscriber);
            });
        });
    }
}
package BlogController;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {

    static String domain = "http://localhost:7000";

    public static void main(String[] args) {

        Javalin app = Javalin.create(Config -> {Config.addStaticFiles("/public");}).start(7000);

        app.routes(() -> {

            before(LoginController.ensureLoginBeforeViewingEditor);

            get("/login", LoginController.serveLoginPage);
            get("/admin", ctx -> ctx.render("templates/adminHome.html.pebble"));
            get("/logout", LoginController.handleLogoutPost);
            post("/wrong/credentials", LoginController.handleLoginPost);

            path("/blog", () -> {
                get("/addData", DatabaseController::addData);
                get("/:pn", BlogController::getBlogHome);
                get("/categories/:pn", CategoriesController::getCategories);
                get("/category/:ctgn/:pn", SingleCategoryController::getDesiredCategory);
                get("/article/:pn", ArticleController::getAricle);
                post("/subscribed", SubscriberController::addSubscriber);
            });
        });
    }
}









//----------------------------- before main
//    enum MyRoles implements Role{
//        ANY_ONE, ADMIN, WRITER;
//    }

//    public static Set<Role> roleSet= new HashSet<Role>();

//    List<Role> prince = Arrays.asList(MyRoles.WRITER, MyRoles.ADMIN);
//    List<Role> anyOne = Arrays.asList(MyRoles.ANY_ONE);
//    List<Role> writer = Arrays.asList(MyRoles.WRITER);
//
//    List<String> princeCreds = List.of("Zahidul Islam Prince", "checkpass");
//    List<String> writerCreds = List.of("P2", "Q2");
//
//    Map<List<Role>, List<String>> map = Map.of(prince, princeCreds, writer, writerCreds);
//
//    static boolean getUserRole(Context ctx, Set<Role> roleSet) {
//        return true;
//    }

//----------------------------after main

//        roleSet.add(MyRoles.WRITER);
//        roleSet.add(MyRoles.ADMIN);
//        roleSet.add(MyRoles.ANY_ONE);

//----------------------------accessManager
//.accessManager((handler, ctx, permittedRoles) -> {
//                                if (ctx.path().startsWith("/blog")) {
//                                    handler.handle(ctx);
//                                } else {
//                                    String currentUser = ctx.sessionAttribute("currentUser");
//                                    System.out.println(currentUser);
//                                    if (currentUser == null) {
//                                        LoginController.handleLoginPost(ctx);
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
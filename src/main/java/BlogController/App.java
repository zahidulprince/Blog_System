package BlogController;

import AdminController.*;
import io.javalin.Javalin;

import static com.sun.deploy.util.BufferUtil.MB;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {

    public static String domain = "http://localhost:7000";

    public static void main(String[] args) {

        Javalin app = Javalin.create(Config -> {
            Config.addStaticFiles("/public");
            Config.requestCacheSize = MB;
        }).start(7000);

        app.routes(() -> {

            before(LoginController.ensureLoginBeforeViewingEditor);

            get("/login", LoginController.serveLoginPage);
            post("/logout", LoginController.handleLogoutPost);
            post("/wrong/credentials", LoginController.handleLoginPost);

            path("/admin", () -> {
                get("/home", AdminHome::getBlogHome);

                get("/addCategory", AddCategoryController::getAddCategory);
                get("/addArticle", AddArticleController::getAddArticle);
                get("/addUser", AddUserController::getAddUser);

                get("/manageCategories", ManageCategoriesController::getManageCategories);
                get("/manageArticles", ManageArticlesController::getManageArticles);
                get("/manageUsers", ManageUserController::getManageUser);

                get("/updateCategory", ManageCategoriesController::getCategoryEditForm);
                get("/select", ManageUserController::askToSelectOptionsToUpdate);
                get("/updateUser", ManageUserController::getUserEditForm);
                get("/updateArticle/:an", ManageArticlesController::getArticleEditForm);

                post("/editCategory", ManageCategoriesController::editCategory);
                post("/editUser", ManageUserController::editUser);
                post("/editArticle/:an", ManageArticlesController::editArticle);

                post("/createCategory", AddCategoryController::addCategory);
                post("/createUser", AddUserController::addUser);
                post("/createArticle", AddArticleController::addArticle);

                post("/deleteCategory/:cn", ManageCategoriesController::deleteCategory);
                post("/deleteUser/:un", ManageUserController::deleteUser);
                post("/deleteArticle/:an", ManageArticlesController::deleteArticle);
            });

            path("/blog", () -> {
                get("/:pn", BlogController::getBlogHome);
                get("/categories/:pn", CategoriesController::getCategories);
                get("/category/:ctgn/:pn", SingleCategoryController::getDesiredCategory);
                get("/article/:pn", ArticleController::getAricle);
                post("/subscribed", SubscriberController::addSubscriber);
            });
        });
    }
}

//TODO 1. Image automatic resize
//     2. Multiple User Admin Interface
//     3. Access Manager
//     4. Local Image Upload




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
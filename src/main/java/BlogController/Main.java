package BlogController;

import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;


public class Main {

    static String domain = "http://localhost:7000";
    static DatabaseController dbController;

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> javalinConfig.addStaticFiles("/public")).start(7000);

        dbController = new DatabaseController();

        app.routes(() -> {

            get("/addData", ctx -> dbController.addData());
            get("/blog/:pn", BlogController::getBlogHome);
            get("/categories/:pn", CategoriesController::getCategories);
            get("/category/:ctgn/:pn", SingleCategoryController::getDesiredCategory);
            get("/article/:pn", ArticleController::getAricle);
            get("/login", ctx -> ctx.render("templates/login.html.pebble"));
            post("/subscribed", SubscriberController::addSubscriber);
            post("/admin", AdminController::getAdmin);

        });

    }
}
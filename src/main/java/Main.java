import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.HashMap;

public class Main {

    static DatabaseController dbController;

    public static void main(String[]args){

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/public");
        });
//
        dbController = new DatabaseController();

        app.start(7000);


        app.get("/addData", ctx -> {
            dbController.addData();
        });

//        app.get("/", ctx -> {
//
//            DatabaseController dbController = new DatabaseController();
//
//            dbController.setCategoryInfo();
//            dbController.setUserInfo();
//            dbController.setUserNameInfo();
//            dbController.setArticleInfo();
//            dbController.transactionAndSessionClose();//
//        });

        app.get("/article", ctx -> {

            HashMap<String, Articles> renderData = new HashMap<>();

            Articles articles = null;

            try {
                Session session = dbController.sf.openSession();
                articles = (Articles) session.load(Articles.class, 1);

                renderData.put("post", articles);
                ctx.render("templates/post.html.pebble", renderData);

                session.close();

            }
            catch (Exception e) {
                System.out.println("Error in data");
                e.printStackTrace();
            }

        });



    }
}
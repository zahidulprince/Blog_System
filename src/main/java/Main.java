import io.javalin.Javalin;
import org.hibernate.Session;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    static DatabaseController dbController;

    public static <renderArticles> void main(String[]args){

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/public");
        });
//
        dbController = new DatabaseController();

        app.start(7000);


        app.get("/addData", ctx -> {
            dbController.addData();
        });


        app.get("/article", ctx -> {

            HashMap<String, Articles> renderData = new HashMap<>();

            Articles articles = null;

            try {
                Session session = dbController.sf.openSession();

                articles = (Articles) session.load(Articles.class, 1);

                renderData.put("articles", articles);

                ctx.render("templates/post.html.pebble", renderData);
                System.out.println(articles);

                session.close();

            } catch (Exception e) {
                System.out.println("Error in data");
                e.printStackTrace();
            }

        });

        app.get("/categories", ctx -> {

            HashMap<String, Articles> renderData = new HashMap<>();

            Articles articlesAndCategory = null;
            Articles articlesAndCategory2 = null;

            try {
                Session session = dbController.sf.openSession();

                articlesAndCategory = (Articles) session.load(Articles.class, 1);
                articlesAndCategory2 = (Articles) session.load(Articles.class, 2);


                renderData.put("articleAndCategory", articlesAndCategory);
                renderData.put("articleAndCategory2", articlesAndCategory2);


                ctx.render("templates/categories.html.pebble", renderData);


                session.close();

            } catch (Exception e) {
                System.out.println("Error in data");
                e.printStackTrace();
            }

        });

        app.get("/categories/:pn", ctx -> {

            List<Category> renderCategory = new ArrayList<>();

            HashMap<String, Object> renderData = new HashMap<>();

            Category category = null;

            try {
                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                Session session = dbController.sf.openSession();

                renderCategory = (List<Category>) session.createQuery("FROM Category order by id desc", Category.class).setFirstResult((pn-1)*2).setMaxResults(2).getResultList();

                renderData.put("categories", renderCategory);
                renderData.put("pn", pn);


                ctx.render("templates/categories.html.pebble", renderData);
                session.close();

            } catch ( Exception e ) {
                System.out.println("check again");
                e.printStackTrace();
            }

        });
    }
}
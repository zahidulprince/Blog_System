import io.javalin.Javalin;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Main {

    static DatabaseController dbController;

    public static <renderArticles> void main(String[] args) {

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

                renderData.put("post", articles);

                ctx.render("templates/post.html.pebble", renderData);
                System.out.println(articles);

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

                Query query = session.createQuery("select count(*) from Category");
                long maxRow = (long) query.uniqueResult();
                System.out.println(maxRow);

                double maxTake = 2;

                double lastPageCheck = Math.ceil(maxRow/maxTake);

                renderCategory = (List<Category>) session.createQuery("FROM Category order by id desc", Category.class).setFirstResult(((pn - 1) * 2)).setMaxResults((int)maxTake).getResultList();

                renderData.put("categories", renderCategory);
                renderData.put("pn", pn);
                renderData.put("lastPageCheck", lastPageCheck);

                ctx.render("templates/categories.html.pebble", renderData);

                session.close();

            } catch (Exception e) {
                System.out.println("check again");
                e.printStackTrace();
            }

        });

        app.get("/blog/:pn", ctx -> {

            List<Articles> renderArticles = new ArrayList<>();

            HashMap<String, Object> renderData = new HashMap<>();

            Articles articles = null;

            try {
                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                Session session = dbController.sf.openSession();

                Query query = session.createQuery("select count(*) from Articles");
                long maxRow = (long) query.uniqueResult();
                System.out.println(maxRow);

                double maxTake = 6;

                double lastPageCheck = Math.ceil(maxRow/maxTake);

                Query query1 = session.createQuery("select max(id) from Articles");
                int article = (int) query1.uniqueResult();
                System.out.println(article);

                articles = (Articles) session.load(Articles.class, article);


                renderArticles = (List<Articles>) session.createQuery("FROM Articles order by id desc", Articles.class).setFirstResult(((pn - 1) * 6)+1).setMaxResults((int)maxTake).getResultList();

                renderData.put("articles", articles);
                renderData.put("moreArticles", renderArticles);
                renderData.put("pn", pn);
                renderData.put("lastPageCheck", lastPageCheck);

                ctx.render("templates/index.html.pebble", renderData);
                System.out.println(lastPageCheck);


                session.close();

            } catch (Exception e) {
                System.out.println("check again");
                e.printStackTrace();
            }

        });
    }
}
import io.javalin.Javalin;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;


public class Main {

    static String domain = "http://localhost:7000";

    static DatabaseController dbController;

    public static void main(String[] args) {



        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/public");
        });

        dbController = new DatabaseController();

        app.start(7000);


        app.get("/addData", ctx -> {
            dbController.addData();
        });


        app.get("/article/:pn", ctx -> {

            HashMap<String, Object> renderData = new HashMap<>();

            Articles articles;

            try {

                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                String originalDomain = domain;

                String descriptions = "check";

                Session session = dbController.sf.openSession();

                articles = session.load(Articles.class, pn);

                renderData.put("post", articles);
                renderData.put("des", descriptions);
                renderData.put("originalDomain", originalDomain);

                ctx.render("templates/post.html.pebble", renderData);

                session.close();

            } catch (Exception e) {
                System.out.println("Error in data");
                e.printStackTrace();
            }

        });

        app.get("/categories/:pn", ctx -> {

            List<Category> renderCategory;
            HashMap<String, Object> renderData = new HashMap<>();

            try {
                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                Session session = dbController.sf.openSession();

                Query query = session.createQuery("select count(*) from Category");
                long maxRow = (long) query.uniqueResult();

                String originalDomain = domain;

                double maxTake = 2;

                double lastPageCheck = Math.ceil(maxRow / maxTake);
                int LastPage = (int) lastPageCheck;

                renderCategory = session.createQuery("FROM Category order by id desc", Category.class).setFirstResult(((pn - 1) * 2)).setMaxResults((int) maxTake).getResultList();

                renderData.put("categories", renderCategory);
                renderData.put("pn", pn);
                renderData.put("lastPageCheck", lastPageCheck);
                renderData.put("goToLastPage", LastPage);
                renderData.put("originalDomain", originalDomain);

                ctx.render("templates/categories.html.pebble", renderData);

                session.close();

            } catch (Exception e) {
                System.out.println("check again");
                e.printStackTrace();
            }

        });

        app.get("/blog/:pn", ctx -> {
            List<Articles> renderArticles;

            HashMap<String, Object> renderData = new HashMap<>();

            Articles articles;

            try {
                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                Session session = dbController.sf.openSession();

//                Getting The Latest Id'd Article
                Query query1 = session.createQuery("select max(id) from Articles");
                int article = (int) query1.uniqueResult();
                System.out.println(article);

                articles = session.load(Articles.class, article);

                //Getting how many rows are in there
                Query query = session.createQuery("select count(*) from Articles");
                long maxRowNT = (long) query.uniqueResult();
                long maxRow = maxRowNT - 1;


                double maxTake = 6;

                double lastPageCheck = Math.ceil(maxRow / maxTake);

                int LastPage = (int) lastPageCheck;

                String originalDomain = domain;


                String path = String.format("%s/blog/", domain);

                //showing all the articles
                renderArticles = (List<Articles>) session.createQuery("FROM Articles order by id desc", Articles.class).setFirstResult(((pn - 1) * 6) + 1).setMaxResults((int) maxTake).getResultList();

                renderData.put("latestArticle", articles);
                renderData.put("moreArticles", renderArticles);
                renderData.put("pn", pn);
                renderData.put("lastPageCheck", lastPageCheck);
                renderData.put("goToLastPage", LastPage);
                renderData.put("index", true);
                renderData.put("subscription", true);
                renderData.put("path", path);
                renderData.put("originalDomain", originalDomain);

                ctx.render("templates/index.html.pebble", renderData);

                session.close();

            } catch (Exception e) {
                System.out.println("check again");
                e.printStackTrace();
            }
        });

        app.post("/subscribed", ctx -> {
            String emailID = ctx.formParam("email");
            dbController.addEmail(emailID);
            ctx.render("templates/subscribed.html.pebble");
        });

        app.get("/category/:ctgn/:pn", ctx -> {
            List<Articles> renderArticles;
            List<Articles> numOfArticles;

            HashMap<String, Object> renderData = new HashMap<>();

            try {
                String ctgn = ctx.pathParam("ctgn");
                int ctgID = Integer.parseInt(ctgn);

                String str = ctx.pathParam("pn");
                int pn = Integer.parseInt(str);

                Session session = dbController.sf.openSession();

                String query = "FROM Articles E where E.category=" + ctgID;

                renderArticles = session.createQuery(query, Articles.class).setFirstResult((pn-1)*6).setMaxResults(6).getResultList();
                numOfArticles = session.createQuery(query, Articles.class).getResultList();

                double listSize = numOfArticles.size();
                double maxTake = 6;
                double lastPageCheck = Math.ceil(listSize / maxTake);
                int LastPage = (int) lastPageCheck;

                String path = String.format("%s/category/%d/", domain, ctgID);
                String originalDomain = domain;

                renderData.put("pn", pn);
                renderData.put("moreArticles", renderArticles);
                renderData.put("path", path);
                renderData.put("goToLastPage", LastPage);
                renderData.put("lastPageCheck", lastPageCheck);
                renderData.put("listSize", listSize);
                renderData.put("originalDomain", originalDomain);

                ctx.render("templates/index.html.pebble", renderData);

                session.close();

            } catch (Exception e) {
                System.out.println("Error in data");
                e.printStackTrace();
            }
        });
    }
}
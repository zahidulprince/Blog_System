package BlogController;

import BlogArchitecture.Articles;

import io.javalin.http.Context;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogController extends App {

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);

        return m.replaceAll("");
    }

    public static void getBlogHome(Context ctx) {

        List<Articles> renderArticles;

        HashMap<String, Object> renderData = new HashMap<>();

        Articles latestArticle;

        try {
            String str = ctx.pathParam("pn");
            int pn = Integer.parseInt(str);

            Session session = DatabaseController.sf.openSession();

//          Getting The Latest Id'd Article
            Query query1 = session.createQuery("select max(id) from Articles");
            int latestArticleID = (int) query1.uniqueResult();

            latestArticle = session.load(Articles.class, latestArticleID);

            String latestArticleDescription = latestArticle.getDescription();
            latestArticleDescription = removeTags(latestArticleDescription);

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
            renderArticles = session.createQuery("FROM Articles order by id desc", Articles.class).setFirstResult(((pn - 1) * 6) + 1).setMaxResults((int) maxTake).getResultList();

            String[] descriptions = new String[6];
            int iterateDescription = 0;

            for (Articles articles1: renderArticles) {
                descriptions[iterateDescription]  = articles1.getDescription();
                iterateDescription++;
            }

            for (int i = 0; i < descriptions.length; i++) {
                descriptions[i] = removeTags(descriptions[i]);
            }

            int test = 0;
            for (Articles articles: renderArticles) {
                articles.setDescription(descriptions[test]);
                test++;
            }

            renderData.put("latestArticle", latestArticle);
            renderData.put("moreArticles", renderArticles);
            renderData.put("pn", pn);
            renderData.put("lastPageCheck", lastPageCheck);
            renderData.put("goToLastPage", LastPage);
            renderData.put("index", true);
            renderData.put("subscription", true);
            renderData.put("path", path);
            renderData.put("originalDomain", originalDomain);
            renderData.put("descriptions", descriptions);
            renderData.put("latestArticleDescription", latestArticleDescription);

            ctx.render("templates/index.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("check again");
            e.printStackTrace();
        }
    }
}

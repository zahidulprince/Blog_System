package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.Category;
import BlogArchitecture.User;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddArticleController extends App {

    static Category categoryToSet;
    static User userToSet;

    public static void getAddArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        renderData.put("form", true);
        renderData.put("originalDomain", domain);

        if (ctx.sessionAttribute("isRedirected") == "true") {
            renderData.put("wrongSubmit", true);
        }
        ctx.render("templates/admin/Article/addArticle.html.pebble", renderData);
    }

    public static void addArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        List<Articles> articlesList;
        List<Category> categoryList;
        List<User> userList;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String articleTitle = ctx.formParam("articleTitle");
        String articleCategory = ctx.formParam("articleCategory");
        String articleWriterEmail = ctx.formParam("articleWriter");
        String articleDescription = ctx.formParam("tinymcetext");
        String articleImageLink = ctx.formParam("articleHeroImageLink");

        boolean isThere = false;
        boolean categoryCheckedRight = false;
        boolean userCheckedRight = false;

        articlesList = s.createQuery("FROM Articles", Articles.class).getResultList();
        categoryList = s.createQuery("FROM Category", Category.class).getResultList();
        userList = s.createQuery("FROM User", User.class).getResultList();

        for (Category category : categoryList) {
            if (articleCategory.equals(category.getName())) {
                categoryToSet = category;
                categoryCheckedRight = true;
                break;
            }
        }


        for (User user : userList) {
            if (articleWriterEmail.equals(user.getEmail())) {
                userToSet = user;
                userCheckedRight = true;
                break;
            }
        }

        if (!categoryCheckedRight || !userCheckedRight) {
            ctx.sessionAttribute("isRedirected", "true");
            ctx.redirect(domain + "/admin/addArticle");
        } else {
            if (articlesList.isEmpty()) {
                createArticle(ctx, renderData, s, tx,articleImageLink, articleDescription, articleTitle,  categoryToSet, userToSet);
            } else {

                for (Articles articles : articlesList) {
                    if (articleTitle.equals(articles.getTitle())) {
                        isThere = true;
                        break;
                    }
                }

                if (!isThere) {
                    createArticle(ctx, renderData, s, tx, articleDescription, articleImageLink, articleTitle,  categoryToSet, userToSet);
                } else {
                    renderData.put("addedAlready", true);
                    renderData.put("originalDomain", domain);
                    ctx.render("templates/admin/Article/addArticle.html.pebble", renderData);
                }
            }
        }
    }

    private static void createArticle(Context ctx, HashMap<String, Object> renderData, Session s, Transaction tx, String articleImageLink, String articleDescription, String articleTitle,  Category categoryToSet, User userToSet) {
        Articles articles = new Articles();
        articles.setTitle(articleTitle);
        articles.setDate(new Date());
        articles.setDescription(articleImageLink);
        articles.setCategory(categoryToSet);
        articles.setUser(userToSet);
        articles.setLink(articleDescription);
        s.save(articles);
        tx.commit();
        s.close();
        renderData.put("added", true);
        renderData.put("originalDomain", domain);
        ctx.render("templates/admin/Article/addArticle.html.pebble", renderData);
    }
}

package BlogController;

import BlogArchitecture.User;

import io.javalin.http.Handler;

import org.hibernate.Session;

import static Util.RequestUtil.*;
import static Util.ViewUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController extends App {

    static Session session = DatabaseController.sf.openSession();

    public static Handler serveLoginPage = ctx -> {

        if (ctx.sessionAttribute("currentUser") != null) {
            ctx.redirect("http://localhost:7000/admin/home");
        } else {
            Map<String, Object> model = baseModel(ctx);
            model.put("loggedOut", removeSessionAttrLoggedOut(ctx));
            model.put("loginRedirect", removeSessionAttrLoginRedirect(ctx));
            ctx.render("templates/login.html.pebble", model);
        }
    };

    public static Handler handleLoginPost = ctx -> {
        Map<String, Object> model = baseModel(ctx);
        HashMap<String, Object> renderData = new HashMap<>();
        if (!authenticate(getQueryUserEmail(ctx), getQueryPassword(ctx))) {
            model.put("authenticationFailed", true);
            renderData.put("wrong", true);
            ctx.render("templates/login.html.pebble", renderData);
        } else {
            ctx.sessionAttribute("currentUser", getQueryUserEmail(ctx));
            model.put("authenticationSucceeded", true);
            model.put("currentUser", getQueryUserEmail(ctx));
            if (getQueryLoginRedirect(ctx) != null) {
                ctx.redirect(getQueryLoginRedirect(ctx));
            }
            ctx.redirect("http://localhost:7000/admin/home");
        }
    };

    private static final List<User> allUsers = session.createQuery("FROM User", User.class).getResultList();

    private static boolean authenticate(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        User user = getUserByUsermail(email);
        if (user == null) {
            return false;
        }
        boolean passwordMatched = false;
        for (User user1 : allUsers) {
            if (password.equals(user1.getPassword())) {
                passwordMatched = true;
                break;
            }
        }
        return passwordMatched;
    }

    private static User getUserByUsermail(String email) {
        return allUsers.stream().filter(b -> b.email.equals(email)).findFirst().orElse(null);
    }

    public static Handler handleLogoutPost = ctx -> {
        ctx.sessionAttribute("currentUser", null);
        ctx.sessionAttribute("loggedOut", "true");
        ctx.redirect("http://localhost:7000/login");
    };

    public static Handler ensureLoginBeforeViewingEditor = ctx -> {
        if (!ctx.path().startsWith("/admin")) {
            return;
        }
        if (ctx.sessionAttribute("currentUser") == null) {
            ctx.sessionAttribute("loginRedirect", ctx.path());
            ctx.redirect("http://localhost:7000/login");
        }
    };
}


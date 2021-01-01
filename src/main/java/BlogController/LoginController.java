package BlogController;

import BlogArchitecture.User;

import io.javalin.http.Handler;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import static Util.RequestUtil.*;
import static Util.ViewUtil.*;

import java.util.List;
import java.util.Map;

public class LoginController extends App {

    static Session session = DatabaseController.sf.openSession();

    public static Handler serveLoginPage = ctx -> {

        if (getSessionCurrentUser(ctx) != null) {
            if (getQueryLoginRedirect(ctx) == null){
                ctx.redirect(domain + "admin/home");
            }else {
                ctx.redirect(getQueryLoginRedirect(ctx));
            }

        } else {
            Map<String, Object> model = baseModel(ctx);
            model.put("loggedOut", removeSessionAttrLoggedOut(ctx));
            ctx.render("templates/login.html.pebble", model);
        }
    };

    public static Handler handleLoginPost = ctx -> {
        Map<String, Object> model = baseModel(ctx);
        if (!authenticate(getQueryUserEmail(ctx), getQueryPassword(ctx))) {
            model.put("authenticationFailed", true);
            model.put("wrong", true);
            ctx.render("templates/login.html.pebble", model);
        } else {
            ctx.sessionAttribute("currentUser", getQueryUserEmail(ctx));
            model.put("authenticationSucceeded", true);
            model.put("currentUser", getQueryUserEmail(ctx));
            if (getQueryLoginRedirect(ctx) == null){
                ctx.redirect(domain + "/admin/home");
            }else {
                ctx.redirect(getQueryLoginRedirect(ctx));
            }
        }
    };

    private static final List<User> allUsers = session.createQuery("FROM User", User.class).getResultList();

    private static boolean authenticate(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        User user = null;

        for (User user1 : allUsers) {
            if (email.equals(user1.getEmail())) {
                user = user1;
                break;
            }
        }

        if (user == null) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
        return hashedPassword.equals(user.getPassword());
    }

    public static Handler handleLogoutPost = ctx -> {
        ctx.sessionAttribute("currentUser", null);
        ctx.sessionAttribute("loginRedirect", null);
        ctx.sessionAttribute("loggedOut", "true");
        ctx.redirect(domain + "/login");
    };

    public static Handler ensureLoginBeforeViewingEditor = ctx -> {
        if (!ctx.path().startsWith("/admin")) {
            return;
        }
        if (getSessionCurrentUser(ctx) == null) {
            ctx.sessionAttribute("loginRedirect", ctx.path());
            ctx.redirect(domain + "/login");
        }
    };
}
package BlogController;

import BlogArchitecture.User;
import Util.ViewUtil;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.hibernate.Session;

import static Util.RequestUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController extends Main {

    static Session session = dbController.sf.openSession();

    public static void serveLoginPage (Context ctx) {
        if (ctx.sessionAttribute("currentUser") != null) {
            ctx.redirect("http://localhost:7000/admin");
        } else {
            Map<String, Object> model = ViewUtil.baseModel(ctx);
            model.put("loggedOut", removeSessionAttrLoggedOut(ctx));
            model.put("loginRedirect", removeSessionAttrLoginRedirect(ctx));
            ctx.render("templates/login.html.pebble", model);
        }
    };

    public static void handleLoginPost (Context ctx) {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        HashMap<String, Object> renderData = new HashMap<>();
        if (!authenticate(getQueryUserEmail(ctx), getQueryPassword(ctx))) {
            model.put("authenticationFailed", true);
            renderData.put("wrong", true);
            System.out.println(renderData);
            ctx.render("templates/login.html.pebble", renderData);
            System.out.println("first");
        } else {
            ctx.sessionAttribute("currentUser", getQueryUserEmail(ctx));
            model.put("authenticationSucceeded", true);
            model.put("currentUser", getQueryUserEmail(ctx));
            if (getQueryLoginRedirect(ctx) != null) {
                ctx.redirect(getQueryLoginRedirect(ctx));
            }
            ctx.redirect("http://localhost:7000/admin");
            System.out.println("second");
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

//    public static void getAdmin(Context ctx) {
//
//        String emailId = ctx.formParam("email");
//        String password = ctx.formParam("password");
//
//        List<User> allUsers;
//
//        try {
//            Session session = dbController.sf.openSession();
//            allUsers = session.createQuery("FROM User", User.class).getResultList();
//
//            for (User allUser : allUsers) {
//                if (emailId.equals(allUser.getEmail()) && password.equals(allUser.getPassword())) {
//                    ctx.result("Accept");
//                    break;
//                }else {
//                    ctx.result("Please, try again!");
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error in data");
//            e.printStackTrace();
//        }
//    }


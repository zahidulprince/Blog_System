package Util;

import BlogController.App;
import io.javalin.http.Context;
import io.javalin.http.ErrorHandler;

import static Util.RequestUtil.*;

import java.util.HashMap;
import java.util.Map;

public class ViewUtil extends App {

    public static Map<String, Object> baseModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", getSessionCurrentUser(ctx));
        model.put("originalDomain", domain);
        return model;
    }

    public static ErrorHandler notFound = ctx -> {
        ctx.result("Not Found");
    };

}
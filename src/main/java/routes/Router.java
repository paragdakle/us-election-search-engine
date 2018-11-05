package routes;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class Router {

    public static void main(String[] args) {

        //GET REST APIS
        get("/hello", (req, res) -> "Current time is " + new Date(System.currentTimeMillis()).toString());

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "Hello World!");
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "templates/home.vm")
            );
        });

        //POST REST APIS

    }

}

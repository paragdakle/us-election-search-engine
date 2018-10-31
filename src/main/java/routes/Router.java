package routes;

import java.util.Date;

import static spark.Spark.get;

public class Router {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Current time is " + new Date(System.currentTimeMillis()).toString());
    }

}

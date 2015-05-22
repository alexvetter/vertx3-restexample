package vertx3.restexample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class RestExampleVerticle extends AbstractVerticle {

    private RestExampleVerticle that = this;
    private Map<String, JsonObject> users = new HashMap<>();

    @Override
    public void start() {
        setUpInitialData();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/users/:userid").handler(that::handleGetUser);
        router.put("/users/:userid").handler(that::handleAddUser);

        router.get("/users").handler(that::handleListUsers);

        router.get("/").handler(that::redirect);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void redirect(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        response.setStatusCode(301);
        response.putHeader("Location", "/users");
        response.end();
    }

    private void handleGetUser(RoutingContext routingContext) {
        String userid = routingContext.request().getParam("userid");

        HttpServerResponse response = routingContext.response();

        if (userid == null) {
            sendError(400, response);
        } else {
            JsonObject user = users.get(userid);
            if (user == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(user.encode());
            }
        }
    }

    private void handleAddUser(RoutingContext routingContext) {
        String userid = routingContext.request().getParam("userid");

        HttpServerResponse response = routingContext.response();

        if (userid == null) {
            sendError(400, response);
        } else {
            JsonObject user = routingContext.getBodyAsJson();
            if (user == null) {
                sendError(400, response);
            } else {
                users.put(userid, user);
                response.end();
            }
        }
    }

    private void handleListUsers(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        users.values().forEach(arr::add);
        routingContext.response().putHeader("content-type", "application/json").end(arr.encode());
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData() {
        addUser(new JsonObject().put("id", "1").put("name", "Test User 1"));
        addUser(new JsonObject().put("id", "2").put("name", "Test User 1"));
        addUser(new JsonObject().put("id", "3").put("name", "Test User 1"));
    }

    private void addUser(JsonObject user) {
        users.put(user.getString("id"), user);
    }
}

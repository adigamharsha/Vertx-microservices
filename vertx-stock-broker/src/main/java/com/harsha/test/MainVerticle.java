package com.harsha.test;

import com.harsha.test.asset.AssetsRestApi;
import com.harsha.test.quote.QuotesRestApi;
import com.harsha.test.watchlist.WatchList;
import com.harsha.test.watchlist.WatchListApi;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  public static final int PORT = 8888;
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhanled Error ,{}", error);
    });
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOG.error("Failed to deploy", ar.cause());
      }
      LOG.info("Deployed Verticle , {}", MainVerticle.class.getName());
    });
  }

  private static Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        return;
      }
      LOG.error("Route Error {}", errorContext.failure());
      errorContext.response().end(new JsonObject().put("message", "Some Thing went Wrong").toBuffer());
    };
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(getAvailableProcessors()))
      .onFailure(startPromise::fail)
      .onSuccess(id ->{
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }

  private static int getAvailableProcessors() {
    return Math.max(1,Runtime.getRuntime().availableProcessors());
  }

}

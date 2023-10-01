package com.harsha.test;

import com.harsha.test.asset.AssetsRestApi;
import com.harsha.test.quote.QuotesRestApi;
import com.harsha.test.watchlist.WatchListApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(RestApiVerticle.class);

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
    startHttpServerAndAttachRoutes(startPromise);
  }

  private void startHttpServerAndAttachRoutes(Promise<Void> startPromise) {
    final Router restApi = Router.router(vertx);
    restApi
      .route()
      .handler(BodyHandler.create())
      .failureHandler(handleFailure());
    AssetsRestApi.fetchAssets(restApi);
    AssetsRestApi.fetchAssetsWIthJson(restApi);
    QuotesRestApi.fetchQuotes(restApi);
    WatchListApi.attachWatchList(restApi);
    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("Http Server Error {}", error))
      .listen(MainVerticle.PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
}

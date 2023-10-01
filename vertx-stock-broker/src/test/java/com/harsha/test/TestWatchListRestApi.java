package com.harsha.test;

import com.harsha.test.asset.Asset;
import com.harsha.test.watchlist.WatchList;
import com.harsha.test.watchlist.WatchListApi;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(TestWatchListRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void test_watchlist_assets_api(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    UUID accountId = UUID.randomUUID();
    webClient
      .put("/account/watchList/"+accountId.toString())
      .sendJsonObject(watchListBuilder())
      .onComplete(testContext.succeeding(response ->{
        var json = response.bodyAsJsonObject();
        LOG.info("Response from PUT ,{}", json);
        assertEquals("{\"assets\":[{\"asset\":\"AMZN\"}]}",response.bodyAsJsonObject().encode());
      }))
      .compose(next ->{
        webClient
          .get("/account/watchList/"+accountId.toString())
          .send()
          .onComplete(testContext.succeeding(response ->{
            var json = response.bodyAsJsonObject();
            LOG.info("Response from GET ,{}", json);
            assertEquals("{\"assets\":[{\"asset\":\"AMZN\"}]}",response.bodyAsJsonObject().encode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  private static JsonObject watchListBuilder() {
    return new WatchList(List.of(Asset.builder().asset("AMZN").build())).mapTo();
  }

  @Test
  void test_quotes_assets_api_unknown(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient
      .get("/quotes/TCS")
      .send()
      .onComplete(testContext.succeeding(response ->{
        var json = response.bodyAsJsonObject();
        assertEquals("{\"quote\":\"for Asset TCS Not found\",\"path\":\"/quotes/TCS\"}",json.encode());
        testContext.completeNow();
      }));
  }
}

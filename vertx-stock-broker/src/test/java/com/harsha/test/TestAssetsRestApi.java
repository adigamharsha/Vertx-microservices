package com.harsha.test;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestAssetsRestApi {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void test_assets_rest_api(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient
      .get("/assets")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonArray();
        assertEquals("[{\"id\":\"1\",\"name\":\"NTFLX\"},{\"id\":\"2\",\"name\":\"AMZN\"},{\"id\":\"3\",\"name\":\"TSLA\"}]", json.encode());
        assertEquals(200, response.statusCode());
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString() , response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        testContext.completeNow();
      }));
  }

  @Test
  void test_quotes_assets_api(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient
      .get("/quotes/AMZN")
      .send()
      .onComplete(testContext.succeeding(response ->{
        var json = response.bodyAsJsonObject().getJsonObject("asset");
        assertEquals("AMZN",json.getString("asset"));
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString() , response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        testContext.completeNow();
      }));
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

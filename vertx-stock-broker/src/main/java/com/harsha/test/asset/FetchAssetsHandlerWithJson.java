package com.harsha.test.asset;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchAssetsHandlerWithJson implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(FetchAssetsHandlerWithJson.class);

  @Override
  public void handle(RoutingContext context) {
    JsonArray responseJson = JsonArray.of(
      new JsonObject().put("1", "NTFLX"),
      new JsonObject().put("2", "AMZN"),
      new JsonObject().put("3", "TSLA"));
    LOG.info("Path invoked {} and Response is {}", context.normalizedPath(), responseJson);
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(responseJson.toBuffer());
  }
}

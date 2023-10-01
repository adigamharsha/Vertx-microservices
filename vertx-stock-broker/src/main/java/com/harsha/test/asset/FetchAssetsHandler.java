package com.harsha.test.asset;

import com.harsha.test.broker.StockInfo;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchAssetsHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(FetchAssetsHandler.class);

  @Override
  public void handle(RoutingContext context) {
    JsonArray responseJson = JsonArray.of(
      StockInfo.builder().id("1").name("NTFLX").build(),
      StockInfo.builder().id("2").name("AMZN").build(),
      StockInfo.builder().id("3").name("TSLA").build());
    LOG.info("Path invoked {} and Response is {}", context.normalizedPath(), responseJson);
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(responseJson.toBuffer());
  }
}

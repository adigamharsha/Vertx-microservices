package com.harsha.test.watchlist;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PutWatchListHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(PutWatchListHandler.class);
  @Override
  public void handle(RoutingContext context){
    var accountId = context.pathParam("accountId");
    JsonObject json = context.getBodyAsJson();
    WatchList watchList = json.mapTo(WatchList.class);
    LOG.info("Account Id is -> {}", accountId);
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(watchList.mapTo().toBuffer());
  }
}

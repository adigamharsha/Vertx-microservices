package com.harsha.test.watchlist;

import com.harsha.test.asset.Asset;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WatchListApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListApi.class);

  public static void attachWatchList(Router restApi){
    restApi.put("/account/watchList/:accountId").handler(new PutWatchListHandler());

    restApi.get("/account/watchList/:accountId").handler(context->{
      var accountId = context.pathParam("accountId");
      LOG.info("Account Id is -> {}", accountId);
      context
        .response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .end(watchListBuilder().toBuffer());
    });

    restApi.delete("/account/watchList/:accountId").handler(context->{
      var accountId = context.pathParam("accountId");
      LOG.info("Account Id is -> {}", accountId);
      context
        .response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .end(watchListBuilder().toBuffer());
    });

  }
  private static JsonObject watchListBuilder() {
    return new WatchList(List.of(Asset.builder().asset("AMZN").build())).mapTo();
  }
}

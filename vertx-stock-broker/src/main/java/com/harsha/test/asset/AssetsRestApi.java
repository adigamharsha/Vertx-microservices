package com.harsha.test.asset;

import com.harsha.test.broker.StockInfo;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetsRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);

  public static void fetchAssets(Router restApi) {
    restApi.get("/assets").handler(new FetchAssetsHandler());
  }

  public static void fetchAssetsWIthJson(Router restApi) {
    restApi.get("/assetsWithJson").handler(new FetchAssetsHandlerWithJson());
  }
}

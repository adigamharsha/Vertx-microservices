package com.harsha.test.quote;

import com.harsha.test.asset.FetchAssetsHandlerWithJson;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class QuotesHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(QuotesHandler.class);
  public final Map<String, Quote> assetMap;

  public QuotesHandler(final Map<String, Quote> assetMap) {
    this.assetMap = assetMap;
  }

  @Override
  public void handle(RoutingContext context) {
    String assets = context.pathParam("assets");
    LOG.info("Assets required for Path, {} , {}", context.normalizedPath(), assets);
    Optional<Quote> quote = Optional.ofNullable(assetMap.get(assets));
    if (quote.isEmpty()) {
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(
          new JsonObject()
            .put("quote", "for Asset " + assets + " Not found")
            .put("path", context.normalizedPath())
            .toBuffer());
      return;
    }
    JsonObject response = quote.get().toJsonObject();
    context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).end(response.toBuffer());
  }
}

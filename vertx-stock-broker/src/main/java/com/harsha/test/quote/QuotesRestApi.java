package com.harsha.test.quote;

import com.harsha.test.asset.Asset;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {

  public static final Logger LOG = LoggerFactory.getLogger(QuotesRestApi.class);

  public static final List<String> QUOTESLIST = List.of("AMZN", "TSLA", "GOOGLE", "NTFLX");

  public static Map<String, Quote> assetMap = new HashMap<>();

  static {
    QUOTESLIST.stream().forEach(quote -> {
      assetMap.put(quote, initRandomQuote(quote));
    });
  }

  public static void fetchQuotes(Router restApi) {
    restApi.get("/quotes/:assets").handler(new QuotesHandler(assetMap));

  }

  private static Quote initRandomQuote(String quote) {
    return Quote.builder()
      .asset(Asset.builder().asset(quote).build())
      .ask(randomValue())
      .bid(randomValue())
      .volume(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}

package com.harsha.test.watchlist;

import com.harsha.test.asset.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchList {
  List<Asset> assets;

  public JsonObject mapTo() {
    return JsonObject.mapFrom(this);
  }
}

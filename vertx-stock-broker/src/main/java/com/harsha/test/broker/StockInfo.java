package com.harsha.test.broker;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StockInfo {
  String id;
  String name;
}

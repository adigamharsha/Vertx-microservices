package com.example.vertx_exmaple;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectTest {

  @Test
  void testJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("1","hello");
    jsonObject.put("2",2);

    String encode = jsonObject.encode();
    JsonObject jsonObject1 =new JsonObject(encode);
    assertEquals("{\"1\":\"hello\",\"2\":2}", encode);
    assertEquals(jsonObject,jsonObject1);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap() {
    final Map<String, Object> myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "Alice");
    myMap.put("loves_vertx", true);
    final JsonObject asJsonObject = new JsonObject(myMap);
    assertEquals(myMap, asJsonObject.getMap());
    assertEquals(1, asJsonObject.getInteger("id"));
    assertEquals("Alice", asJsonObject.getString("name"));
    assertEquals(true, asJsonObject.getBoolean("loves_vertx"));
  }

  @Test
  void jsonArrayCanBeMapped() {
    final JsonArray myJsonArray = new JsonArray();
    myJsonArray
      .add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3))
      .add("randomValue")
    ;
    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomValue\"]", myJsonArray.encode());
  }

  @Test
  void mappingJavaObjects(){
    final Person p  = new Person(1,"harsha");
    final JsonObject mappedJson = JsonObject.mapFrom(p);
    assertEquals("{\"id\":1,\"name\":\"harsha\"}", mappedJson.encode());
  }
}

class Person{
  int id;
  String name;
  public Person(){}
  public Person(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

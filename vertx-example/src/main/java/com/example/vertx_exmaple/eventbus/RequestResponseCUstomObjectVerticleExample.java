package com.example.vertx_exmaple.eventbus;

import com.example.vertx_exmaple.model.PingObject;
import com.example.vertx_exmaple.model.PongObject;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseCUstomObjectVerticleExample {


  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle(),logOnError());
    vertx.deployVerticle(new ResponseVerticle(),logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        System.out.println("err"+ar.cause());
      }
    };
  }

  public static class RequestVerticle extends AbstractVerticle{
//    private static final Logger RequestLog = LoggerFactory.getLogger(RequestVerticle.class);
    private static final String ADDRESS ="myrequest.address";
    @Override
    public void start(Promise<Void> startPromise){
      var eventBus = vertx.eventBus();
      PingObject ping = PingObject.builder().id(1).name("ivy").build();
      String REQUEST = "This is Request";
      System.out.println("Custom Request Sent, {}"+ping);
      eventBus.registerDefaultCodec(PingObject.class, new LocalMessageCodec<>(PingObject.class));
      eventBus.<PongObject>request(ADDRESS,ping, reply->{
        if(reply.failed()){
          System.out.println("Failed ->"+reply.cause());
        }
        System.out.println("Response Arrived: {}"+reply.result().body());
      });
      startPromise.complete();
    }

  }

  public static class ResponseVerticle extends AbstractVerticle{
    private static final Logger ResponseLog = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise){
      vertx.eventBus().registerDefaultCodec(PongObject.class, new LocalMessageCodec<>(PongObject.class));
      vertx.eventBus().<PingObject>consumer(RequestVerticle.ADDRESS, message->{
        System.out.println("Request Received, {}"+message.body());
        message.reply(PongObject.builder().id(2).name("ivies").build());
      }).exceptionHandler(error ->{
        System.out.println("Error ->"+error);
      });
      startPromise.complete();
    }
  }
}

package com.example.vertx_exmaple.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseVerticleExample {


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  public static class RequestVerticle extends AbstractVerticle{
//    private static final Logger RequestLog = LoggerFactory.getLogger(RequestVerticle.class);
    private static final String ADDRESS ="myrequest.address";
    @Override
    public void start(Promise<Void> startPromise){
      startPromise.complete();
      EventBus eventBus = vertx.eventBus();
      String REQUEST = "This is Request";
      eventBus.<String>request(ADDRESS,REQUEST, reply->{
        System.out.println("Response Arrived: {}"+reply.result().body());
      });

    }

  }

  public static class ResponseVerticle extends AbstractVerticle{
    private static final Logger ResponseLog = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise){
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS,message->{
        System.out.println("Request Received, {}"+message.body());
        message.reply("Response sent ::");
      });
    }
  }
}

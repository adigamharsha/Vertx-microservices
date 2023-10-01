package com.example.vertx_exmaple.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class PointtoPointMessageVertcile {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SenderVerticle());
    vertx.deployVerticle(new Consumerverticle());
  }

  public static class SenderVerticle extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000,id->{
        vertx.eventBus().send(SenderVerticle.class.getName(),"This message is from Sender");
      });
    }
  }

  public static class Consumerverticle extends AbstractVerticle{

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(SenderVerticle.class.getName(),message->{
        System.out.println("Received Message from ->"+SenderVerticle.class.getName());
      });
    }
  }
}

package com.example.vertx_exmaple.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Slf4j
public class PubSubVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new SubcriberVerticle1());
    vertx.deployVerticle(SubcriberVerticle2.class.getName(), new DeploymentOptions().setInstances(2));
  }

  public static class PublisherVerticle extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id -> {
        vertx.eventBus().publish(PublisherVerticle.class.getName(), "This message is from Publisher");
      });
    }
  }

  public static class SubcriberVerticle1 extends AbstractVerticle {
    private static final Logger Sub1Log = LoggerFactory.getLogger(SubcriberVerticle1.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(PublisherVerticle.class.getName(), message -> {
        Sub1Log.info("Message received , {}", message.body());
        System.out.println("Message received , {}"+message.body());
      });
    }
  }

  public static class SubcriberVerticle2 extends AbstractVerticle {
    private static final Logger Sub2Log = LoggerFactory.getLogger(SubcriberVerticle2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(PublisherVerticle.class.getName(), message -> {
        Sub2Log.info("Message received , {}", message.body());
        System.out.println("Message received , {}"+message.body());
      });
    }
  }
}

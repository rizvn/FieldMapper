package com.rizvn.fieldmapper.conf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @author Riz
 */
@Configuration
@ComponentScan("com.rizvn.fieldmapper")
public class MainConf {
  static CountDownLatch latch = new CountDownLatch(1);

  public static void main(String [] args) throws Exception{
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(MainConf.class);
    ctx.refresh();
    latch.await();
  }
}

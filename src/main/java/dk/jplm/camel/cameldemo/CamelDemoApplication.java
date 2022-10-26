package dk.jplm.camel.cameldemo;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"dk.jplm.camel.cameldemo"})
@SpringBootApplication
public class CamelDemoApplication {

    @Autowired
    ProducerTemplate producerTemplate;

    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }

}

package dk.jplm.camel.cameldemo.routes;

import dk.jplm.camel.cameldemo.dto.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

import static dk.jplm.camel.cameldemo.config.CamelConfiguration.RABBIT_URI;
import static org.apache.camel.LoggingLevel.ERROR;

@Component
public class WeatherRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // consumer (consume from) = from, producer (produce data to) = to
        // exchange - queue - routingKey
        /*
        from("rabbitmq:amq.direct?queue=weather&routingKey=weather&autoDelete=false").
                log("Before enrichment: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .process(this::enrichWeatherDto)
                .log("After enrichment ${body}")
                .marshal().json(JsonLibrary.Jackson, WeatherDto.class).
                //    .log(LoggingLevel.ERROR, "Got this message from Rabbit: ${body}");
                        to("rabbitmq:amq.direct?queue=weather-event&routingKey=weather-event&autoDelete=false")
                .to("file://C:/Users/jplm/Desktop/Projects/Camel-demo/files/?fileName=weather-events.txt&fileExist=Append");
                */

            fromF(RABBIT_URI, "weather", "weather")
                    .log(ERROR, "Before Enrichment: ${body}")
                    .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                    .process(this::enrichWeatherDto)
                    .log(ERROR, "After Enrichment: ${body}")
                    .marshal().json(JsonLibrary.Jackson, WeatherDto.class)
                    .toF(RABBIT_URI, "weather-events", "weather-events")
                    .to("file://C:/Users/jplm/Desktop/Projects/Camel-demo/files/?fileName=weather-events.txt&fileExist=Append")
            ;
    }

    private void enrichWeatherDto(Exchange exchange) {
        WeatherDto weatherDto = exchange.getMessage().getBody(WeatherDto.class);
        weatherDto.setReceivedTime(new Date().toString());

        Message message = new DefaultMessage(exchange);
        message.setBody(weatherDto);
        exchange.setMessage(message);
    }
}

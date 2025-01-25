package kz.seisen.delongi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DelongiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DelongiApplication.class, args);
    }

}

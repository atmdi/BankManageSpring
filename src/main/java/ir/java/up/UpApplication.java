package ir.java.up;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableTransactionManagement
@EnableMongoRepositories
@SpringBootApplication
@EnableJpaRepositories
public class UpApplication {

    public static void main(String[] args) {

        SpringApplication.run(UpApplication.class, args);



    }

}

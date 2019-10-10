package ru.bazhen.jooq;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.bazhen.jooq.service.impl.OrganizationServiceImpl;
import ru.bazhen.jooq.service.impl.WorkerServiceImpl;

@SpringBootApplication
public class App {
    @Bean
    CommandLineRunner demo (OrganizationServiceImpl organizationServiceImpl, WorkerServiceImpl workerServiceImpl){
        return args -> {
//            workerService.getAllWorkers()
//                    .forEach(System.out::println);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}

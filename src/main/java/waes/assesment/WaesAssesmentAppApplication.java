package waes.assesment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class WaesAssesmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaesAssesmentAppApplication.class, args);
    }

}

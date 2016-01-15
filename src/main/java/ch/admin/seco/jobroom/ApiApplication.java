package ch.admin.seco.jobroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//
// Notes:
//
// Don't set @EnableWebMvc annotation, and let spring-boot magic happen
// See http://springfox.github.io/springfox/docs/current/#configuring-the-objectmapper
//

@SpringBootApplication
// TODO @Import({ WebAppConfig.class })
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}

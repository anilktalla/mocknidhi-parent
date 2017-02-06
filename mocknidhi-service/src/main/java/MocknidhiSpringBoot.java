import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "com.mocknidhi")
public class MocknidhiSpringBoot {

    public static void main(String args[]){
        new SpringApplication(MocknidhiSpringBoot.class).run(args);
    }

}

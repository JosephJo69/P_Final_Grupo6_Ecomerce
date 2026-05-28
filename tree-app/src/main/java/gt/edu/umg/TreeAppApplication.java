package gt.edu.umg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TreeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TreeAppApplication.class, args);
    }
}

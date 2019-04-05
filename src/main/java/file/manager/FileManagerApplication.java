package file.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "file.manager")
public class FileManagerApplication {
    private static final Logger log = LoggerFactory.getLogger(FileManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FileManagerApplication.class);
        application.run(args);
        log.info("FilesInfo manager is up!");
    }
}

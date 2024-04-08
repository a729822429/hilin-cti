package icu.hilin.cti.biz;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BizApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BizApplication.class)
                .run(args);
    }

}

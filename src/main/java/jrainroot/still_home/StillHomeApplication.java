package jrainroot.still_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// 해당 어노테이션 추가해야 BaseEntity의 생성일 수정일 자동기록 기능 활성화
@EnableJpaAuditing
public class StillHomeApplication {

    public static void main(String[] args) {

        Hello hello = new Hello();
        hello.setData("hello");
        String data = hello.getData();
        System.out.println("data = " + data);

        SpringApplication.run(StillHomeApplication.class, args);
    }

}

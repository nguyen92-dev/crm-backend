package vn.io.nguyen32.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"top.nguyennd", "vn.io.nguyen32.crm"})
public class CrmApplication {

	static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}

}

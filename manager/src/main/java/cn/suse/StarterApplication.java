package cn.suse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
@SpringBootApplication
@MapperScan(basePackages="cn.suse.dao")
public class StarterApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>{
	public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
        System.out.println("=================== manager successfully start ==========================");
    }
	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.setPort(80);
	}
}

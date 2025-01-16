package pl.szlify.codingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import pl.szlify.codingapi.security.XSSFilter;

@SpringBootApplication
public class CodingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingApiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<XSSFilter> xssFilter() {
		FilterRegistrationBean<XSSFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new XSSFilter());
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}
}

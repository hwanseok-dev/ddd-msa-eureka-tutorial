package hwanseok.server.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
// standard bean으로 정의하고, component scan에 포함되도록 함
//@EnableWebMvc
// Jackson을 찾고 default JSON, XML converters를 등록
// @SpringBootApplication을 Main에서 사용하며 spring-webmvc를 포함하고 있으면 @EnableWebMvc가 자동으로 등록된다.
public class WebConfig {
}

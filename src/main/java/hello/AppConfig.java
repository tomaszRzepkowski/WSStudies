package hello;

import hello.security.AuthorizationFilter;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tomaszrzepkowski on 16.03.2017.
 */
@Configuration
public class AppConfig {

    @Bean
    public Filter doFilter() {
        System.out.println("siema");
        return new AuthorizationFilter();
    }

}

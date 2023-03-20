package sa.springframework.spring6restmvc.config;


import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {


    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1905";

    private static final String BASE_URL = "http://localhost:8084";



    @Bean
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){
        // assert configurer != null; // This is not needed because of @NonNull annotation
         RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory(BASE_URL);
        return builder.uriTemplateHandler(defaultUriBuilderFactory)
                .basicAuthentication(USERNAME,PASSWORD);
    }

}

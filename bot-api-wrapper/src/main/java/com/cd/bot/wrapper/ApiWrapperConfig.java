package com.cd.bot.wrapper;

import com.cd.bot.wrapper.http.BotCameraService;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.wrapper" })
@PropertySources({
        @PropertySource("classpath:api-wrapper-application.properties"),
        @PropertySource("file:${app.home}/api-wrapper-application.properties") //wins
})
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
public class ApiWrapperConfig {

    @Autowired
    private Environment environment;

    @Bean
    public String apiUrl() {
        return environment.getRequiredProperty("api.url");
    }

    @Bean
    @Qualifier("apiRestTemplate")
    public RestTemplate apiRestTemplate() throws Exception {
        return new RestTemplate(httpClientRequestFactory());
    }

    @Bean(name = "httpClient")
    public HttpClient httpClient() throws Exception {
        return HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory())
                .build();
    }

    @Bean
    public SSLConnectionSocketFactory sslSocketFactory() {
        final String keystoreLocation = environment.getRequiredProperty("keystore.location");
        final String trustStoreLocation = environment.getRequiredProperty("trust.store.location");
        final String keystorePassword = environment.getRequiredProperty("keystore.password");
        final String trustStorePassword = environment.getRequiredProperty("trust.store.password");

        try {
            KeyStore clientStore = KeyStore.getInstance("JKS");
            clientStore.load(new FileInputStream(keystoreLocation), keystorePassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, keystorePassword.toCharArray());

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword.toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                            .loadKeyMaterial(clientStore, keystorePassword.toCharArray())
                            .build(),
                    NoopHostnameVerifier.INSTANCE);

            return socketFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Bean
    public ClientHttpRequestFactory httpClientRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

}
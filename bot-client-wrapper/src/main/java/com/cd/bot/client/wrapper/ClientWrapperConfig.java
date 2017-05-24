package com.cd.bot.client.wrapper;

import com.cd.bot.wrapper.http.BotClientRestClient;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.client.wrapper" })
@PropertySources({
        @PropertySource("classpath:wrapper-application.properties"),
        @PropertySource("file:${app.home}/wrapper-application.properties") //wins
})
public class ClientWrapperConfig {

    @Autowired
    private Environment environment;

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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Bean
    public ClientHttpRequestFactory httpClientRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean(name = "clientRestTemplate")
    public RestTemplate restTemplate() throws Exception {
        return new RestTemplate(httpClientRequestFactory());
    }

    @Bean
    public BotClientRestClient botCameraService() {
        return new BotClientRestClient();
    }

}
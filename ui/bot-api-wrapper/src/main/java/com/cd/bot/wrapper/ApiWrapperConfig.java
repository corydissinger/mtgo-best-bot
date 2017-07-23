package com.cd.bot.wrapper;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

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
        return HttpClientBuilder.create()
                                .setSSLSocketFactory(sslSocketFactory())
                                .build();
    }

    @Bean(name = "sslSocketFactory")
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
            trustStore.getCertificate("orchestrator");

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            Certificate cert = trustStore.getCertificate("orchestrator");

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(cert.getEncoded()));

            SSLContext context = SSLContext.getInstance("TLSv1");
            context.init(new KeyManager[] { new FilteredKeyManager((X509KeyManager)kmf.getKeyManagers()[0], x509Certificate, "orchestrator") },
                    trustManagerFactory.getTrustManagers(), new SecureRandom());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);

            return socketFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class FilteredKeyManager implements X509KeyManager {
        private final X509KeyManager originatingKeyManager;
        private final X509Certificate sslCertificate;
        private final String SSLCertificateKeyStoreAlias;

        /**
         * @param originatingKeyManager,       original X509KeyManager
         * @param sslCertificate,              X509Certificate to use
         * @param SSLCertificateKeyStoreAlias, Alias of the certificate in the provided keystore
         */
        public FilteredKeyManager(X509KeyManager originatingKeyManager, X509Certificate sslCertificate, String SSLCertificateKeyStoreAlias) {
            this.originatingKeyManager = originatingKeyManager;
            this.sslCertificate = sslCertificate;
            this.SSLCertificateKeyStoreAlias = SSLCertificateKeyStoreAlias;
        }

        @Override
        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            return SSLCertificateKeyStoreAlias;
        }

        @Override
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            return originatingKeyManager.chooseServerAlias(keyType, issuers, socket);
        }

        @Override
        public X509Certificate[] getCertificateChain(String alias) {
            return new X509Certificate[]{ sslCertificate };
        }

        @Override
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return originatingKeyManager.getClientAliases(keyType, issuers);
        }

        @Override
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return originatingKeyManager.getServerAliases(keyType, issuers);
        }

        @Override
        public PrivateKey getPrivateKey(String alias) {
            return originatingKeyManager.getPrivateKey(alias);
        }
    }

    @Bean
    public ClientHttpRequestFactory httpClientRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

}
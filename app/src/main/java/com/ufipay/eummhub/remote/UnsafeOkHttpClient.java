package com.ufipay.eummhub.remote;

import android.util.Log;

import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;

public class UnsafeOkHttpClient {

    private final static String TAG = "UnsafeHttp";

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            if (chain.length==1){
                                Log.d(TAG, "checkClientTrusted: is self signed ??");
                                return;
                            }
                            try {
                                chain[0].checkValidity();
                            } catch (Exception e) {
                                try {
                                    throw new CertificateException("Certificate not valid or trusted.");
                                } catch (CertificateException certificateException) {
                                    Log.d(TAG, "checkClientTrusted: ", certificateException);
                                }
                            }
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            if (chain.length==1) {
                                Log.d(TAG, "checkServerTrusted: is self signed ??");
                                return;
                            }
                            try {
                                chain[0].checkValidity();
                            } catch (Exception e) {
                                try {
                                    throw new CertificateException("Certificate not valid or trusted.");
                                } catch (CertificateException certificateException) {
                                    Log.d(TAG, "checkServerTrusted: ", certificateException);
                                }
                            }
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); // using TLSv1.2 instead of SSL cuz' SSL have been proved to be insecure
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            builder.readTimeout(360, TimeUnit.SECONDS);
            builder.writeTimeout(360, TimeUnit.SECONDS);
            builder.connectTimeout(360, TimeUnit.SECONDS);
            builder.protocols(Arrays.asList(Protocol.HTTP_1_1));
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

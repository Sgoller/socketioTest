package com.boshim.www.sockettest;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2017/12/21 11:06
 */
//If you would like to connect to server without SSL, or the server enables SSL
//with a certificate issued by a well-known CA, please use getToken and no
// need to set ConnectionOptions.
//If you would like to connect to server with SSL enabled with a self-signed
// certificate,
//please use getTokenSSLSelfsigned. And pass SSLContext and/or
// HostnameVerifier to join() by ConnectionOptions.
//For debug, if you would like to trust all certificates and hostnames,
//please use getTokenSSLINSECURE. And pass SSLContext and/or HostnameVerifier
// to join() by ConnectionOptions.

public class HttpTokenBus {

    private static final String TAG = "HttpTokenBus";

    private final boolean isDebug = true;

    private String mTokenUrl = "createToken/";

    public SSLContext getSslContext() {
        return sslContext;
    }

    private SSLContext sslContext = null;

    public HttpTokenBus(){

    }

    public void setTokenUrl(String tokenUrl) {
        mTokenUrl = tokenUrl;
    }

    //******************WARNING****************
    //DO NOT IMPLEMENT THIS IN PRODUCTION CODE
    //*****************************************
    private HostnameVerifier hostnameVerifier;

    /**
     * @Author 张皓【zhanghao@boshim.com】
     * @Description:http的方式
     *
     * @Date 2017/12/21 11:13
     * @param basicServer
     * @param roomId
     **/
    public String httpGetToken(String basicServer, String roomId) {
        return doGetToken(basicServer, roomId, false);
    }

    /**
     * @Author 张皓【zhanghao@boshim.com】
     * @Description:https的方式
     *
     * @Date 2017/12/21 11:13
     * @param basicServer
     * @param roomId
     **/
    public String httpsGetToken(Context context, int certId, String basicServer, String roomId ) {
        if (isDebug){
            setUpINSECURESSLContext();
        }else {
            setUpSelfsignedSSLContext(context,certId);
        }
        return doGetToken(basicServer, roomId, true);
    }

    //This is to set up an SSL context enabled with a self-signed certificate.
    private void setUpSelfsignedSSLContext(Context context, int certId) {
        InputStream caInput;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //democert.crt is a demo certificate, you need to substitute it with a proper one.
            //caInput = WooGeenActivity.this.getResources().openRawResource(R.raw.democert);
            caInput = context.getResources().openRawResource(certId);
            Certificate ca = cf.generateCertificate(caInput);
            caInput.close();

            // Create a KeyStore containing the trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (CertificateException | IOException | NoSuchAlgorithmException |
                KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }


    //******************WARNING****************
    //DO NOT IMPLEMENT THIS IN PRODUCTION CODE
    //*****************************************
    public void setUpINSECURESSLContext() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }
        };

        hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public HostnameVerifier getHostnameVerifier(){
        return hostnameVerifier;
    }

    @NonNull
    private String doGetToken(String basicServer, String roomId, boolean isSecure) {
        StringBuilder token = new StringBuilder("");
        URL url;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(basicServer + mTokenUrl);
            if (isSecure) {
                httpURLConnection = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(
                        sslContext.getSocketFactory());
                if (hostnameVerifier != null) {
//                    LogUtil.w(TAG, "YOU ARE NOT VERIFYING THE HOSTNAME");
//                    LogUtil.w(TAG, "DO NOT IMPLEMENT THIS IN PRODUCTION CODE");
                    //******************WARNING****************
                    //DO NOT IMPLEMENT THIS IN PRODUCTION CODE
                    //*****************************************
                    ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(hostnameVerifier);
                }
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");

            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("role", "presenter");
            jsonObject.put("username", "user");
            jsonObject.put("room", roomId.equals("") ? "" : roomId);
            out.write(jsonObject.toString().getBytes("UTF-8"));
            out.flush();
            out.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "UTF-8");
                    token.append(lines);
                }
                reader.close();
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return token.toString();
    }
}

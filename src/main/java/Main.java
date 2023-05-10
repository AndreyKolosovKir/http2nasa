import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=4GNGe8xw54FaNruUAcsUQVz926qRNWgk4KYr1Qn9";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        ObjectMapper mapper = new ObjectMapper();

        Nasa nasa = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );

        String urlImage = nasa.getUrl();
        HttpGet request2 = new HttpGet(urlImage);
        CloseableHttpResponse response1 = httpClient.execute(request2);
        File file = new File(urlImage.substring(urlImage.lastIndexOf("/") + 1));
        response1.getEntity().writeTo(new FileOutputStream(file));

        response.close();
        response1.close();
        httpClient.close();
    }
}
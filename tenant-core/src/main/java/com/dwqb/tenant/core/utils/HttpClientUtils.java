package com.dwqb.tenant.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by huangdong on 16/8/28.
 */
public class HttpClientUtils {

    private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class);



    public static String post(String uri, HttpEntity entity, Header... headers) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost method = new HttpPost(uri);
        if (!ArrayUtils.isEmpty(headers)) {
            for (Header header : headers) {
                method.addHeader(header);
            }
        }
        try {
            method.setEntity(entity);
            HttpResponse result = client.execute(method);
            int sc = result.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(result.getEntity());
            if (sc != HttpStatus.SC_OK) {
                String message = "[POST][" + uri + "]error response status!!! sc=" + sc;
                return null;
            }
            LOGGER.info("[POST][" + uri + "] end... content=" + content);
            return content;
        } catch (Throwable e) {
            String message = "[POST][" + uri + "]" + e.getMessage();
            LOGGER.error(message, e);
            throw e;
        } finally {
            method.releaseConnection();
            client.close();
        }
    }
}

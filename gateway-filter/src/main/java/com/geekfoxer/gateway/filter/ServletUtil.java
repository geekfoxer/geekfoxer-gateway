package com.geekfoxer.gateway.filter;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
public class ServletUtil {


    public static <T> Enumeration<T> emptyEnumeration() {
        return Collections.enumeration(Collections.<T>emptySet());
    }

    public static <T> Enumeration<T> enumeration(Collection<T> collection) {
        if (collection == null) {
            return emptyEnumeration();
        }
        return Collections.enumeration(collection);
    }

    public static <T> Enumeration<T> enumerationFromKeys(Map<T, ?> map) {
        if (map == null) {
            return emptyEnumeration();
        }
        return Collections.enumeration(map.keySet());
    }

    public static <T> Enumeration<T> enumerationFromValues(Map<?, T> map) {
        if (map == null) {
            return emptyEnumeration();
        }
        return Collections.enumeration(map.values());
    }


    /**
     * Parse the character encoding from the specified content type header. If the content type is
     * null, or there is no explicit character encoding, <code>null</code> is returned.
     *
     * @param contentType a content type header
     */
    public static String getCharsetFromContentType(String contentType) {

        if (contentType == null) {
            return null;
        }
        int start = contentType.indexOf("charset=");
        if (start < 0) {
            return null;
        }
        String encoding = contentType.substring(start + 8);
        int end = encoding.indexOf(';');
        if (end >= 0) {
            encoding = encoding.substring(0, end);
        }
        encoding = encoding.trim();
        if ((encoding.length() > 2) && (encoding.startsWith("\"")) && (encoding.endsWith("\""))) {
            encoding = encoding.substring(1, encoding.length() - 1);
        }
        return encoding.trim();

    }

    public static Collection<Cookie> getCookies(String name, HttpRequest request) {
        String cookieString = request.headers().get(HttpHeaderNames.COOKIE);
        if (cookieString != null) {
            List<Cookie> foundCookie = new ArrayList<>();
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
            for (Cookie cookie : cookies) {
                if (cookie.name().equals(name)) {
                    foundCookie.add(cookie);
                }
            }

            return foundCookie;
        }
        return null;
    }

    public static String getMimeType(String fileUrl) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(fileUrl);
    }

    public static String sanitizeUri(String uri) {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, StandardCharsets.ISO_8859_1.name());
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        // Convert file separators.
        uri = uri.replace('/', File.separatorChar);

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + ".") || uri.contains("." + File.separator)
                || uri.startsWith(".") || uri.endsWith(".")) {
            return null;
        }

        return uri;
    }

    public static Collection<Locale> parseAcceptLanguageHeader(String acceptLanguageHeader) {

        if (acceptLanguageHeader == null) {
            return null;
        }

        List<Locale> locales = new ArrayList<>();

        for (String str : acceptLanguageHeader.split(",")) {
            String[] arr = str.trim().replace("-", "_").split(";");

            // Parse the locale
            Locale locale = null;
            String[] l = arr[0].split("_");
            switch (l.length) {
                case 2:
                    locale = new Locale(l[0], l[1]);
                    break;
                case 3:
                    locale = new Locale(l[0], l[1], l[2]);
                    break;
                default:
                    locale = new Locale(l[0]);
                    break;
            }

            locales.add(locale);
        }

        return locales;

    }

}

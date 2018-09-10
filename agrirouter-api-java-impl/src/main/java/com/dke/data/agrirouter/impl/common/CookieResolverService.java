package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotFetchCookiesException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Resolve the cookies after login to use them for further requests.
 */
public class CookieResolverService extends EnvironmentalService {

    private static final HashMap<String, Set<Cookie>> cookieCache = new HashMap<>();

    public CookieResolverService(Environment environment) {
        super(environment);
    }

    public Set<Cookie> cookies(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new IllegalArgumentException("Please provide valid - means no blank values - values for username and password.");
        }

        Set<Cookie> cookies = new HashSet<>();

        Optional<Set<Cookie>> cookiesFromCache = this.fetchCookiesFromCache(username);

        if (cookiesFromCache.isPresent()) {
            if (this.isAnyCookieExpired(cookiesFromCache.get())) {
                cookies = fetchCookiesFromAgrirouter(username, password);
            } else {
                cookies = cookiesFromCache.get();
            }
        } else {
            cookies = fetchCookiesFromAgrirouter(username, password);
        }
        return cookies;
    }

    private Set<Cookie> fetchCookiesFromAgrirouter(String username, String password) {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);

            final String url = this.environment.getAgrirouterLoginUrl();
            final HtmlPage page = webClient.getPage(url);

            final HtmlTextInput usernameTextInput = page.getHtmlElementById("j_username");
            usernameTextInput.setText(username);

            final HtmlPasswordInput passwordTextInput = page.getHtmlElementById("j_password");
            passwordTextInput.setText(password);

            final HtmlButton submitInput = page.getHtmlElementById("logOnFormSubmit");
            submitInput.click();

            Set<Cookie> cookiesFromWebClient = webClient.getCookieManager().getCookies();
            cookieCache.put(username, cookiesFromWebClient);
            return cookiesFromWebClient;

        } catch (IOException e) {
            throw new CouldNotFetchCookiesException(e);
        }
    }

    private boolean isAnyCookieExpired(Set<Cookie> cookies) {
        return null == cookies || cookies.isEmpty() || cookies.stream().anyMatch(cookie -> cookie.getExpires() != null && cookie.getExpires().before(Date.from(Instant.now())));
    }

    private Optional<Set<Cookie>> fetchCookiesFromCache(String username) {
        return Optional.ofNullable(cookieCache.get(username));
    }

}
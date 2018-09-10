package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.env.QA;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CookieResolverServiceTest {

    @Test
    void givenInvalidUsernameOrPassword_Resolve_ShouldFail() {
        CookieResolverService cookieResolverService = new CookieResolverService(new QA());
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies("", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies(" ", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies("sascha.doemer@lmis.de", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies("sascha.doemer@lmis.de", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cookieResolverService.cookies("sascha.doemer@lmis.de", " "));
    }

    @Test
    void givenValidUsernameOrPassword_Resolve_ShouldReturnAListOfCookies() {
        CookieResolverService cookieResolverService = new CookieResolverService(new QA());
        Set<Cookie> cookies = cookieResolverService.cookies("sascha.doemer@lmis.de", "kzas@ZC8P*k3");
        Assertions.assertNotNull(cookies);
        Assertions.assertEquals(7, cookies.size());
    }


}

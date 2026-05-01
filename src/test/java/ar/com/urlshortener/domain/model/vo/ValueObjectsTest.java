package ar.com.urlshortener.domain.model.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @ParameterizedTest
    @ValueSource(strings = {"https://google.com", "http://test.org"})
    void shouldCreateValidOriginalUrl(String url) {
        OriginalUrl originalUrl = new OriginalUrl(url);
        assertEquals(url, originalUrl.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"google.com", "ftp://test.org", "", "   "})
    void shouldThrowExceptionForInvalidOriginalUrl(String url) {
        assertThrows(IllegalArgumentException.class, () -> new OriginalUrl(url));
    }

    @Test
    void shouldCreateValidShortCode() {
        ShortCode shortCode = new ShortCode("abc123");
        assertEquals("abc123", shortCode.value());
    }

    @Test
    void shouldThrowExceptionForInvalidShortCode() {
        assertThrows(IllegalArgumentException.class, () -> new ShortCode(""));
        assertThrows(IllegalArgumentException.class, () -> new ShortCode(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "192.168.1.1", "255.255.255.255"})
    void shouldCreateValidIpAddress(String ip) {
        IpAddress ipAddress = new IpAddress(ip);
        assertEquals(ip, ipAddress.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"256.0.0.1", "abc.def", "", "   "})
    void shouldThrowExceptionForInvalidIpAddress(String ip) {
        assertThrows(IllegalArgumentException.class, () -> new IpAddress(ip));
    }

    @Test
    void shouldAcceptValidIpv6() {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IpAddress ip = new IpAddress(ipv6);
        assertEquals(ipv6, ip.value());
    }

    @Test
    void shouldThrowExceptionForNullShortCode() {
        assertThrows(IllegalArgumentException.class, () -> new ShortCode(null));
    }

    @Test
    void shouldThrowExceptionForNullUserAgent() {
        assertThrows(IllegalArgumentException.class, () -> new UserAgent(null));
    }
}

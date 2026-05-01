package ar.com.urlshortener.domain.model.vo;

import java.util.Objects;

public record IpAddress(String value) {

    private static final String IPV4_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final String IPV6_PATTERN =
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";

    public IpAddress {
        if(Objects.isNull(value) || value.isBlank()){
            throw new IllegalArgumentException("UserAgent no puede ser Null");
        }

        if (!isValidIPv4(value) && !isValidIPv6(value)) {
            throw new IllegalArgumentException("Formato IP address invalido: " + value);
        }
    }

    private static boolean isValidIPv4(String ip) {
        return ip.matches(IPV4_PATTERN);
    }

    private static boolean isValidIPv6(String ip) {
        return ip.matches(IPV6_PATTERN);
    }

}

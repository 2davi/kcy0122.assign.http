package kr.letech.assign.http;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final Integer code;
    private final String reason;

    HttpStatus(Integer code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public Integer code() {
        return code;
    }

    public String reason() {
        return reason;
    }

    /** 숫자로 enum 찾기 */
    public static HttpStatus fromCode(Integer code) {
        for (HttpStatus s : values()) {
            if (s.code == code) return s;
        }
        return null;
    }
}


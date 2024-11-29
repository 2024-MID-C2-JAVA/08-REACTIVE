package co.com.sofka.cuentaflex.libs.infrastructure.utils.din;

import java.time.LocalDateTime;

public final class DinError {
    private String type;
    private LocalDateTime date;
    private String source;
    private String code;
    private String providerErrorCode;
    private String message;
    private String detail;

    public static DinError getDefaultDinError() {
        return new DinError(
                DinErrorType.SUCCESS,
                "Server",
                "0000",
                null,
                "Successful operation.",
                "Successful operation."
        );
    }

    public static DinError getUnknownError() {
        return new DinError(
                DinErrorType.ERROR,
                "Unknown",
                "1006",
                null,
                "Unknown error.",
                "Unknown error."
        );
    }

    public DinError() {
    }

    public DinError(DinErrorType type, String source, String code, String providerErrorCode, String message, String detail) {
        this.date = LocalDateTime.now();
        this.type = type.name();
        this.source = source;
        this.code = code;
        this.providerErrorCode = providerErrorCode;
        this.message = message;
        this.detail = detail;
    }

    public DinError(String type, String source, String code, String providerErrorCode, String message, String detail) {
        this.date = LocalDateTime.now();
        this.type = type;
        this.source = source;
        this.code = code;
        this.providerErrorCode = providerErrorCode;
        this.message = message;
        this.detail = detail;
    }

    public DinError(LocalDateTime date, String type, String source, String code, String providerErrorCode, String message, String detail) {
        this.type = type;
        this.source = source;
        this.date = date;
        this.code = code;
        this.providerErrorCode = providerErrorCode;
        this.message = message;
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProviderErrorCode() {
        return providerErrorCode;
    }

    public void setProviderErrorCode(String providerErrorCode) {
        this.providerErrorCode = providerErrorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

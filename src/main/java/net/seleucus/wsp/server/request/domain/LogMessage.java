package net.seleucus.wsp.server.request.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class LogMessage {

    private final String ipAddress;
    private final String userId;
    private final long timestamp;
    private final HttpMethod httpMethod;
    private final String requestUri;
    private final HttpVersion httpVersion;
    private final int responseCode;
    private final long responseSizeBytes;

    public LogMessage(final String ipAddress, final String userId, final long timestamp, final HttpMethod httpMethod,
                      final String requestUri, final HttpVersion httpVersion, final int responseCode,
                      final long responseSizeBytes) {
        this.ipAddress = ipAddress;
        this.userId = userId;
        this.timestamp = timestamp;
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
        this.responseCode = responseCode;
        this.responseSizeBytes = responseSizeBytes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getResponseSizeBytes() {
        return responseSizeBytes;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class LogMessageBuilder {
        private String ipAddress;
        private String userId;
        private long timestamp;
        private HttpMethod httpMethod;
        private String requestUri;
        private HttpVersion httpVersion;
        private int responseCode;
        private long responseSizeBytes;

        public static LogMessageBuilder createLogMessage(){
            return new LogMessageBuilder();
        }

        public LogMessageBuilder withIpAddress(final String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public LogMessageBuilder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        public LogMessageBuilder withTimestamp(final long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public LogMessageBuilder withHttpMethod(final HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public LogMessageBuilder withRequestUri(final String requestUri) {
            this.requestUri = requestUri;
            return this;
        }

        public LogMessageBuilder withHttpVersion(final HttpVersion httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public LogMessageBuilder withResponseCode(final int responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public LogMessageBuilder withResponseSizeBytes(final long responseSizeBytes) {
            this.responseSizeBytes = responseSizeBytes;
            return this;
        }

        public LogMessage build(){
            return new LogMessage(ipAddress, userId, timestamp, httpMethod, requestUri, httpVersion, responseCode, responseSizeBytes);
        }
    }
}

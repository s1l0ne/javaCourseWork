package com.example.nl2sqlui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "nl2sql")
public class AppProperties {

    private FastApi fastapi = new FastApi();
    private Context context = new Context();

    public FastApi getFastapi() { return fastapi; }
    public void setFastapi(FastApi fastapi) { this.fastapi = fastapi; }

    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    public static class FastApi {
        private String baseUrl = "http://127.0.0.1:8000";
        private String userId = "ui-demo";

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }

    public static class Context {
        private boolean enabled = true;
        private List<String> allowedSchemas = new ArrayList<>();
        private List<String> distinctColumns = new ArrayList<>();
        private int distinctLimit = 20;
        private int cacheTtlSec = 300;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public List<String> getAllowedSchemas() { return allowedSchemas; }
        public void setAllowedSchemas(List<String> allowedSchemas) { this.allowedSchemas = allowedSchemas; }

        public List<String> getDistinctColumns() { return distinctColumns; }
        public void setDistinctColumns(List<String> distinctColumns) { this.distinctColumns = distinctColumns; }

        public int getDistinctLimit() { return distinctLimit; }
        public void setDistinctLimit(int distinctLimit) { this.distinctLimit = distinctLimit; }

        public int getCacheTtlSec() { return cacheTtlSec; }
        public void setCacheTtlSec(int cacheTtlSec) { this.cacheTtlSec = cacheTtlSec; }
    }
}

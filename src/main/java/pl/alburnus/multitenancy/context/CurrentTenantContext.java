package pl.alburnus.multitenancy.context;

import org.springframework.util.Assert;

public class CurrentTenantContext {

    private static ThreadLocal<String> threadTenantContext = new ThreadLocal<>();

    public static void setThreadTenantContext(String tenant) {
        Assert.notNull(tenant, "error: tenant can't be null!");
        threadTenantContext.set(tenant);
    }

    public static String getThreadTenantContext() {
        return threadTenantContext.get();
    }
}

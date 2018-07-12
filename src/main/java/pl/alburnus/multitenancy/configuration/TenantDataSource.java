package pl.alburnus.multitenancy.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import pl.alburnus.multitenancy.context.CurrentTenantContext;

public class TenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return CurrentTenantContext.getThreadTenantContext();
    }
}

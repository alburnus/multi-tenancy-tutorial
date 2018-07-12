package pl.alburnus.multitenancy.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class TenantConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String TENANTS_FOLDER = "tenants";
    private static final String TENANT_NAME_STORE = "tenant-name";

    // Main properties application.properties
    private final DataSourceProperties dataSourceProperties;

    public TenantConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    @ConfigurationProperties(
            prefix = "spring.datasource"
    )
    public DataSource dataSource() {
        File[] files = Paths.get(TENANTS_FOLDER).toFile().listFiles();
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        for (File propertyFile : files) {
            Properties properties = new Properties();
            DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(this.getClass().getClassLoader());

            try {
                properties.load(new FileInputStream(propertyFile));

                String tenantId = properties.getProperty(TENANT_NAME_STORE);
                // driver z głownego properties
                dataSourceBuilder.driverClassName(dataSourceProperties.getDriverClassName())
                        .url(properties.getProperty("datasource.url"))
                        .username(properties.getProperty("datasource.username"))
                        .password(properties.getProperty("datasource.password"));

                if (Objects.nonNull(dataSourceProperties.getType())) {
                    dataSourceBuilder.type(dataSourceProperties.getType());
                }

                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
            } catch (IOException e) {
                log.error(e.getMessage());
                return null;
            }
        }

        // Potrzebna jest domyslna baza do podłączenia.
        // Musi byc pusta.
        TenantDataSource dataSource = new TenantDataSource();
        dataSource.setDefaultTargetDataSource(defaultDataSource());
        dataSource.setTargetDataSources(resolvedDataSources);

        // Finalizuje inicjalizację zrodla danych
        dataSource.afterPropertiesSet();

        return dataSource;
    }

    private DataSource defaultDataSource() {
        DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(this.getClass().getClassLoader())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword());

        if (Objects.nonNull(dataSourceProperties.getType())) {
            dataSourceBuilder.type(dataSourceProperties.getType());
        }

        return dataSourceBuilder.build();
    }
}
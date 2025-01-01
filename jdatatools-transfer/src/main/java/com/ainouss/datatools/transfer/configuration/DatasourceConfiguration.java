package com.ainouss.datatools.transfer.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class DatasourceConfiguration {

    private Map<String, DataSourceProperties> datasource;

}

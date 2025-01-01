package com.ainouss.datatools.transfer.configuration;

import com.ainouss.datatools.transfer.store.DataSourceRegistry;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataSourcesLoader implements CommandLineRunner {

    private final DatasourceConfiguration configuration;

    @Override
    public void run(String... args) {
        this.configuration.getDatasource()
                .forEach(((name, ds) -> DataSourceRegistry.INSTANCE.createDataSource(name, ds.getUrl(), ds.getUsername(), ds.getPassword())));
    }
}
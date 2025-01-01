package com.ainouss.datatools.transfer.configuration;

import lombok.Data;

@Data
public class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String packageName;

}

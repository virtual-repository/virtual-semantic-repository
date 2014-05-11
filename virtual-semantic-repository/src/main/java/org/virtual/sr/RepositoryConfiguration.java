package org.virtual.sr;

import static org.virtualrepository.Utils.*;
import java.net.URI;
import java.util.Properties;

public class RepositoryConfiguration {

    private final Properties properties;

    public RepositoryConfiguration(Properties properties) {
        this.properties = properties;

        notNull("configuration", properties);
        notNull("staging_endpoint_update", properties.getProperty("staging_endpoint_update"));
        notNull("staging_endpoint_data", properties.getProperty("staging_endpoint_data"));
        notNull("staging_endpoint_query", properties.getProperty("staging_endpoint_query"));
        notNull("public_endpoint_query", properties.getProperty("public_endpoint_query"));
        notNull("public_endpoint_data", properties.getProperty("public_endpoint_data"));
        notNull("query discovery query", properties.getProperty("query_all_sdmx_codelist"));
    }

    public URI staging_endpoint_update() {
        return URI.create(properties.getProperty("staging_endpoint_update"));
    }

    public URI staging_endpoint_data() {
        return URI.create(properties.getProperty("staging_endpoint_data"));
    }

    public URI staging_endpoint_query() {
        return URI.create(properties.getProperty("staging_endpoint_query"));
    }

    public URI public_endpoint_query() {
        return URI.create(properties.getProperty("public_endpoint_query"));
    }
    
    public URI public_endpoint_data() {
        return URI.create(properties.getProperty("public_endpoint_data"));
    }

    public String query_all_sdmx_codelist() {
        return properties.getProperty("query_all_sdmx_codelist");
    }

    public String query_get_codelist(String codelistUri) {
        String p = properties.getProperty("query_get_codelist");
        return p.replace("cl_uri", codelistUri);
    }
}

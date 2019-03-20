package com.kodnito.restapi.rest;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.*;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.*;
import java.util.Set;


@RequestScoped
@Path("hello")
public class HelloWorldEndpoint {

    @Inject
    @ConfigProperty(name = "username", defaultValue = "admin")
    private String username;

    @Inject
    Config config;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        Map<String, Object> configProperties = new HashMap<>();

        configProperties.put("username", username);
        configProperties.put("password", config.getValue("password", String.class));
        configProperties.put("application.server", config.getValue("application.server", String.class));
		
		System.out.println("mpCfg sources: \n");
        Iterable<ConfigSource> cfgSources =  config.getConfigSources();
        for(ConfigSource source: cfgSources){
        	System.out.println(source.getName());
        }
        System.out.println("\n\nmpCfg values from each source: \n");
        for(ConfigSource source: cfgSources){
        	Set<String> configNames = source.getPropertyNames();
        	System.out.println("\nmpCfg name-value pairs from source: " + source.getName());
        	for (String cfgName: configNames) {
        		System.out.println("name: " + cfgName + " value: " + source.getValue(cfgName));      		
        	}
        }

        return Response.ok(configProperties).build();
    }
}
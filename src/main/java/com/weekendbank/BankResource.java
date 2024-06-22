package com.weekendbank;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class BankResource {
    private final String template;
    private final String defaultName;

    public BankResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    public String sayHello() {
        final String value = String.format(template, defaultName);
        return value;
    }
}
package com.weekendbank;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BankApplication extends Application<BankConfiguration> {
    public static void main(String[] args) throws Exception {
        new BankApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BankConfiguration> bootstrap) {
        // optional: bootstrap settings
    }

    @Override
    public void run(BankConfiguration configuration, Environment environment) {
        final BankResource resource = new BankResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
    }
}
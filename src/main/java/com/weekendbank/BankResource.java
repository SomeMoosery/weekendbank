package com.weekendbank;

import unit.java.sdk.ApiClient;
import unit.java.sdk.ApiException;
import unit.java.sdk.api.CreateAccountApi;
import unit.java.sdk.api.CreateApplicationApi;
import unit.java.sdk.model.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

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
    public String sayHello() throws ApiException {
        // Initialize Unit bearer token
        String accessToken = System.getenv("UNIT_BEARER_TOKEN");

        // Initialize API client and add bearer intercept
        ApiClient apiClient = new ApiClient();
        apiClient.setRequestInterceptor(r -> r.header("Authorization", "Bearer " + accessToken));

        // Initialize application for an individual customer
        CreateIndividualApplicationAttributes attr = new CreateIndividualApplicationAttributes();

        // Set name
        FullName fn = new FullName();
        fn.setFirst("Peter");
        fn.setLast("Parker");
        attr.setFullName(fn);

        // Set address
        Address address = new Address();
        address.setStreet("20 Ingram St");
        address.setCity("Forest Hills");
        address.setPostalCode("11375");
        address.setCountry("US");
        address.setState("NY");
        attr.setAddress(address);

        // Set other details
        attr.setSsn("721074426");
        attr.setDateOfBirth(LocalDate.parse("2001-08-10"));
        attr.setEmail("peter@oscorp.com");
        Phone p = new Phone();
        p.setNumber("5555555555");
        p.setCountryCode("1");
        attr.setPhone(p);
        attr.setIdempotencyKey("3a1a33be-4e12-4603-9ed0-820922389fb8");
        attr.setOccupation(Occupation.ARCHITECTORENGINEER);

        // Create request
        CreateIndividualApplication createIndividualApplication = new CreateIndividualApplication();
        createIndividualApplication.setAttributes(attr);

        // Create request and populate it with data
        CreateApplication createApplicationRequest = new CreateApplication();
        createApplicationRequest.data(new CreateApplicationData(createIndividualApplication));

        // Make request
        CreateApplicationApi createApiClient = new CreateApplicationApi(apiClient);
        UnitCreateApplicationResponse res = createApiClient.execute(createApplicationRequest);

        // Cast response (generic Application) to an Individual Application
        IndividualApplication individualApplication = (IndividualApplication) res.getData();

        // Create attributes for deposit account we're going to create
        CreateDepositAccountAttributes createDepositAccountAttributes = new CreateDepositAccountAttributes();
        createDepositAccountAttributes.setDepositProduct("checking");
        createDepositAccountAttributes.setIdempotencyKey(UUID.randomUUID().toString());
        String customerId = individualApplication.getRelationships().getCustomer().getData().getId();

        // Create relationship to associate deposit account with the customer, who was created when the application approved
        CreateDepositAccountRelationships createDepositAccountRelationships = new CreateDepositAccountRelationships();
        CustomerLinkageData customerLinkageData = new CustomerLinkageData();
        customerLinkageData.setType(CustomerLinkageData.TypeEnum.CUSTOMER);
        customerLinkageData.setId(customerId);
        CustomerLinkage customerLinkage = new CustomerLinkage();
        customerLinkage.setData(customerLinkageData);
        createDepositAccountRelationships.customer(customerLinkage);

        // Create the overall CreateDepositAccount object
        CreateDepositAccount createDepositAccount = new CreateDepositAccount();
        createDepositAccount.setAttributes(createDepositAccountAttributes);
        createDepositAccount.setRelationships(createDepositAccountRelationships);

        // Request to create an account
        CreateAccount createAccountRequest = new CreateAccount();
        createAccountRequest.data(new CreateAccountData(createDepositAccount));
        CreateAccountApi createAccountApi = new CreateAccountApi(apiClient);
        UnitAccountResponse accountResponse = createAccountApi.execute(createAccountRequest);

        // TODO deposit into the account

        // TODO cleanup hello world stuff
        return String.format(template, defaultName);
    }
}
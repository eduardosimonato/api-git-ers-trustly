package com.example;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.ws.ApiGitResource;

public class ApiGitResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ApiGitResource.class);
    }

    /**
     * Test to see a message is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("v1/scrapgit/").request().get(String.class);
        assertEquals("Url is not a GIT Url!!", responseMsg);
    }
}

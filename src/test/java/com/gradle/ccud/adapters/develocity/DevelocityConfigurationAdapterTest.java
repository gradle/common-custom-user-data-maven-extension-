package com.gradle.ccud.adapters.develocity;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.develocity.agent.gradle.DevelocityConfiguration;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DevelocityConfigurationAdapterTest extends BaseAdapterTest {

    private DevelocityConfiguration configuration;
    private DevelocityAdapter adapter;

    @BeforeEach
    void setup() {
        configuration = createDevelocityMock();
        adapter = DevelocityAdapter.create(configuration);
    }

    @Test
    @DisplayName("server configuration can be set via an adapter")
    void testServer() {
        //given
        String server = "https://server.gradle.com";
        Property<String> prop = mockPropertyReturning(server);
        when(configuration.getServer()).thenReturn(prop);

        // when
        adapter.setServer(server);

        // then
        verify(prop).set(server);
        assertEquals(server, adapter.getServer());
    }

    @Test
    @DisplayName("project ID can be set via an adapter")
    void testProjectId() {
        //given
        String project = "project";
        Property<String> prop = mockPropertyReturning(project);
        when(configuration.getProjectId()).thenReturn(prop);

        // when
        adapter.setProjectId(project);

        // then
        verify(prop).set(project);
        assertEquals(project, adapter.getProjectId());
    }

    @Test
    @DisplayName("allowUntrustedServer can be set via an adapter")
    void testAllowUntrustedServer() {
        //given
        Property<Boolean> allowUntrustedProp = mockPropertyReturning(false);
        when(configuration.getAllowUntrustedServer()).thenReturn(allowUntrustedProp);

        // when
        adapter.setAllowUntrustedServer(false);

        // then
        verify(configuration.getAllowUntrustedServer()).set(false);

        // when
        adapter.getAllowUntrustedServer();

        // then
        verify(configuration.getAllowUntrustedServer()).get();
    }

    @Test
    @DisplayName("access key can be set via an adapter")
    void testAccessKey() {
        //given
        String accessKey = "key";
        Property<String> prop = mockPropertyReturning(accessKey);
        when(configuration.getAccessKey()).thenReturn(prop);

        // when
        adapter.setAccessKey(accessKey);

        // then
        verify(prop).set(accessKey);
        assertEquals(accessKey, adapter.getAccessKey());
    }

    @Test
    @DisplayName("build cache class can be set via an adapter")
    void testBuildCache() {
        // when
        adapter.getBuildCache();

        // then
        verify(configuration).getBuildCache();
    }

}

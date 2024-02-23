package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildScanExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GradleEnterpriseExtensionAdapterTest extends BaseAdapterTest {

    private GradleEnterpriseExtension extension;
    private DevelocityAdapter adapter;

    @BeforeEach
    void setup() {
        extension = createGradleEnterpriseMock();
        adapter = DevelocityAdapter.create(extension);
    }

    @Test
    @DisplayName("can set and retrieve the server value using adapter")
    void testServer() {
        //given
        String server = "https://ge-server.com";

        // when
        adapter.setServer(server);

        // then
        verify(extension).setServer(server);

        // when
        when(extension.getServer()).thenReturn(server);

        // then
        assertEquals(server, adapter.getServer());
    }

    @Test
    @DisplayName("can set and retrieve the project ID value using adapter")
    void testProjectId() {
        //given
        String projectId = "awesomeProject";

        // when
        adapter.setProjectId(projectId);

        // then
        verify(extension).setProjectId(projectId);

        // when
        when(extension.getProjectId()).thenReturn(projectId);

        // then
        assertEquals(projectId, adapter.getProjectId());
    }

    @Test
    @DisplayName("can set and retrieve the allowUntrustedServer value using adapter")
    void testAllowUntrustedServer() {
        // when
        adapter.setAllowUntrustedServer(true);

        // then
        verify(extension).setAllowUntrustedServer(true);

        // when
        when(extension.getAllowUntrustedServer()).thenReturn(true);

        // then
        assertTrue(adapter.getAllowUntrustedServer());
    }

    @Test
    @DisplayName("can set and retrieve the access key value using adapter")
    void testAccessKey() {
        // given
        String accessKey = "key";

        // when
        adapter.setAccessKey(accessKey);

        // then
        verify(extension).setAccessKey(accessKey);

        // when
        when(extension.getAccessKey()).thenReturn(accessKey);

        // then
        assertEquals(accessKey, adapter.getAccessKey());
    }

    @Test
    @DisplayName("can retrieve the build cache class using adapter")
    void testBuildCache() {
        // when
        adapter.getBuildCache();

        // then
        verify(extension).getBuildCache();
    }

    @Test
    @DisplayName("can configure the build scan extension using an action")
    void testBuildScanAction() {
        // given
        BuildScanExtension buildScanExtension = mock();
        when(extension.getBuildScan()).thenReturn(buildScanExtension);

        // when
        adapter.buildScan(buildScan -> {
            buildScan.setUploadInBackground(true);
            buildScan.setTermsOfServiceUrl("server");
        });

        // then
        verify(buildScanExtension).setUploadInBackground(true);
        verify(buildScanExtension).setTermsOfServiceAgree("server");
    }

}

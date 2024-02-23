package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.BuildScanAdapter;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.ccud.adapters.shared.BuildResultAdapter;
import com.gradle.ccud.adapters.shared.PublishedBuildScanAdapter;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildResult;
import com.gradle.scan.plugin.BuildScanCaptureSettings;
import com.gradle.scan.plugin.BuildScanDataObfuscation;
import com.gradle.scan.plugin.BuildScanExtension;
import com.gradle.scan.plugin.PublishedBuildScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuildScanExtensionAdapterTest extends BaseAdapterTest {

    private DevelocityAdapter gradleEnterprise;
    private BuildScanExtension extension;
    private BuildScanAdapter adapter;

    @BeforeEach
    void setup() {
        GradleEnterpriseExtension geExtension = createGradleEnterpriseMock();
        extension = geExtension.getBuildScan();

        gradleEnterprise = DevelocityAdapter.create(geExtension);
        adapter = gradleEnterprise.getBuildScan();
    }

    @Test
    @DisplayName("can set tags using the extension adapter")
    void testTag() {
        // when
        adapter.tag("tag");

        // then
        verify(extension).tag("tag");
    }

    @Test
    @DisplayName("can set custom values using the extension adapter")
    void testValue() {
        // when
        adapter.value("name", "value");

        // then
        verify(extension).value("name", "value");
    }

    @Test
    @DisplayName("can set custom links using the extension adapter")
    void testLink() {
        // when
        adapter.link("name", "value");

        // then
        verify(extension).link("name", "value");
    }

    @Test
    @DisplayName("can set and retrieve the terms of service URL using the extension adapter")
    void testTermsOfServiceUrl() {
        // given
        String url = "https://terms-of-service.com";

        // when
        adapter.setTermsOfServiceUrl(url);

        // then
        verify(extension).setTermsOfServiceUrl(url);

        // when
        when(extension.getTermsOfServiceUrl()).thenReturn(url);

        // then
        assertEquals(url, adapter.getTermsOfServiceUrl());
    }

    @Test
    @DisplayName("can set and retrieve the terms of service agreement using the extension adapter")
    void testTermsOfServiceAgree() {
        // given
        String agree = "yes";

        // when
        adapter.setTermsOfServiceAgree(agree);

        // then
        verify(extension).setTermsOfServiceAgree(agree);

        // when
        when(extension.getTermsOfServiceAgree()).thenReturn(agree);

        // then
        assertEquals(agree, adapter.getTermsOfServiceAgree());
    }

    @Test
    @DisplayName("can set and retrieve the uploadInBackground value using adapter")
    void testUploadInBackground() {
        // when
        adapter.setUploadInBackground(true);

        // then
        verify(extension).setUploadInBackground(true);

        // when
        when(extension.isUploadInBackground()).thenReturn(true);

        // then
        assertTrue(adapter.isUploadInBackground());
    }

    @Test
    @DisplayName("can configure the build scan publication using adapter")
    void testPublishing() {
        // when
        adapter.publishAlways();
        adapter.publishAlwaysIf(true);
        adapter.publishOnFailure();
        adapter.publishOnFailureIf(false);

        // then
        verify(extension).publishAlways();
        verify(extension).publishAlwaysIf(true);
        verify(extension).publishOnFailure();
        verify(extension).publishOnFailureIf(false);
    }

    @Test
    @DisplayName("can configure the build scan extension using the background action")
    void testBackgroundAction() {
        // given
        doExecuteActionWith(extension).when(extension).background(any());

        // when
        adapter.background(buildScan -> {
            buildScan.setUploadInBackground(true);
            buildScan.setTermsOfServiceUrl("server");
        });

        // then
        verify(extension).setUploadInBackground(true);
        verify(extension).setTermsOfServiceUrl("server");
    }

    @Test
    @DisplayName("can run the build finished action using the adapter")
    void testBuildFinishedAction() {
        // given
        Throwable failure = new RuntimeException("Boom!");
        BuildResult buildResult = mock();
        when(buildResult.getFailure()).thenReturn(failure);

        // when
        ArgCapturingAction<BuildResultAdapter> capturer = new ArgCapturingAction<>();
        adapter.buildFinished(capturer);

        // then
        assertEquals(Collections.singleton(failure), capturer.getValue().getFailures());
    }

    @Test
    @DisplayName("can run the build scan published action using the adapter")
    void testBuildScanPublishedAction() {
        // given
        PublishedBuildScan scan = mock();
        when(scan.getBuildScanId()).thenReturn("scanId");
        doExecuteActionWith(scan).when(extension).buildScanPublished(any());

        // when
        ArgCapturingAction<PublishedBuildScanAdapter> capturer = new ArgCapturingAction<>();
        adapter.buildScanPublished(capturer);

        // then
        assertEquals("scanId", capturer.getValue().getBuildScanId());
    }

    @Test
    @DisplayName("can configure the data obfuscation using an action")
    void testObfuscationAction() {
        // given
        BuildScanDataObfuscation obfuscation = mock();
        when(extension.getObfuscation()).thenReturn(obfuscation);

        // when
        adapter.obfuscation(o -> {
            o.hostname(it -> "<obfuscated>");
            o.username(it -> "<obfuscated>");
        });

        // then
        verify(obfuscation).hostname(any());
        verify(obfuscation).username(any());
    }

    @Test
    @DisplayName("can configure the data capturing using an action")
    void testCaptureAction() {
        // given
        BuildScanCaptureSettings capture = mock();
        when(extension.getCapture()).thenReturn(capture);

        // when
        adapter.capture(c -> {
            c.setBuildLogging(true);
            c.setTestLogging(false);
        });

        // then
        verify(capture).setBuildLogging(true);
        verify(capture).setTestLogging(false);
    }
}

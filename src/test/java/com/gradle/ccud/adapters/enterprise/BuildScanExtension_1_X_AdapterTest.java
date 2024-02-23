package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.shared.BuildResultAdapter;
import com.gradle.ccud.adapters.shared.PublishedBuildScanAdapter;
import com.gradle.ccud.proxies.ProxyFactory;
import com.gradle.scan.plugin.BuildResult;
import com.gradle.scan.plugin.BuildScanExtension;
import com.gradle.scan.plugin.PublishedBuildScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BuildScanExtension_1_X_AdapterTest extends BaseAdapterTest {

    private BuildScanExtension extension;
    private BuildScanExtension_1_X_Adapter adapter;

    @BeforeEach
    void setup() {
        extension = mock();
        // bypass Gradle version check
        adapter = new BuildScanExtension_1_X_Adapter(ProxyFactory.createProxy(extension, BuildScanExtension.class));
    }

    @Test
    @DisplayName("can set the server but not retrieve it")
    void testServer() {
        // given
        String server = "https://irrelevant.com";

        // when
        adapter.setServer(server);

        // then
        verify(extension).setServer(server);
        assertNull(adapter.getServer());
    }

    @Test
    @DisplayName("configuring project ID is not supported")
    void testProjectId() {
        // when
        adapter.setProjectId("irrelevant");
        adapter.getProjectId();

        // then
        verifyNoInteractions(extension);
    }

    @Test
    @DisplayName("can set the allow untrusted property but not retrieve it")
    void testAllowUntrusted() {
        // when
        adapter.setAllowUntrustedServer(true);

        // then
        verify(extension).setAllowUntrustedServer(true);
        assertFalse(adapter.getAllowUntrustedServer());
    }

    @Test
    @DisplayName("configuring access key is not supported")
    void testAccessKey() {
        // when
        adapter.setAccessKey("irrelevant");
        adapter.getAccessKey();

        // then
        verifyNoInteractions(extension);
    }

    @Test
    @DisplayName("build cache class is not available")
    void testBuildCache() {
        // expect
        assertNull(adapter.getBuildCache());
        verifyNoInteractions(extension);
    }

    @Test
    @DisplayName("tags can be set via an adapter")
    void testTag() {
        //given
        String tag = "tag";

        // when
        adapter.getBuildScan().tag(tag);

        // then
        verify(extension).tag(tag);
    }

    @Test
    @DisplayName("values can be set via an adapter")
    void testValue() {
        //given
        String name = "name";
        String value = "value";

        // when
        adapter.getBuildScan().value(name, value);

        // then
        verify(extension).value(name, value);
    }

    @Test
    @DisplayName("links can be set via an adapter")
    void testLink() {
        //given
        String name = "name";
        String value = "https://value.com";

        // when
        adapter.getBuildScan().link(name, value);

        // then
        verify(extension).link(name, value);
    }

    @Test
    @DisplayName("terms of service URL can be set via an adapter but not retrieved")
    void testTermsOfServiceUrl() {
        //given
        String value = "https://value.com";

        // when
        adapter.getBuildScan().setTermsOfServiceUrl(value);

        // then
        verify(extension).setTermsOfServiceUrl(value);

        // and
        assertNull(adapter.getBuildScan().getTermsOfServiceUrl());
    }

    @Test
    @DisplayName("terms of service agreement can be set via an adapter but not retrieved")
    void testTermsOfServiceAgree() {
        //given
        String value = "yes";

        // when
        adapter.getBuildScan().setTermsOfServiceAgree(value);

        // then
        verify(extension).setTermsOfServiceAgree(value);
        assertNull(adapter.getBuildScan().getTermsOfServiceAgree());
    }

    @Test
    @DisplayName("background upload cannot be configured")
    void testUploadInBackground() {
        // when
        adapter.getBuildScan().setUploadInBackground(true);

        // then
        assertFalse(adapter.getBuildScan().isUploadInBackground());
        verifyNoInteractions(extension);
    }

    @Test
    @DisplayName("multiple properties can be configured via buildScan action")
    void testBuildScanAction() {
        // when
        adapter.buildScan(b -> {
            b.setTermsOfServiceUrl("value");
            b.setTermsOfServiceAgree("yes");
        });

        // then
        verify(extension).setTermsOfServiceUrl("value");
        verify(extension).setTermsOfServiceAgree("yes");
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
        doExecuteActionWith(buildResult).when(extension).buildFinished(any());

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
    @DisplayName("obfuscation cannot be configured")
    void testObfuscation() {
        // expect
        assertNull(adapter.getBuildScan().getObfuscation());
    }

    @Test
    @DisplayName("capture cannot be configured")
    void testCapture() {
        // expect
        assertNull(adapter.getBuildScan().getCapture());
    }
}

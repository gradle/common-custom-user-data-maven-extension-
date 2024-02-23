package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.BuildScanAdapter;
import com.gradle.ccud.adapters.BuildScanCaptureAdapter;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildScanCaptureSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

public class BuildScanCaptureSettingsAdapterTest extends BaseAdapterTest {

    private BuildScanAdapter buildScan;
    private BuildScanCaptureSettings capture;
    private BuildScanCaptureAdapter adapter;

    @BeforeEach
    void setup() {
        GradleEnterpriseExtension extension = createGradleEnterpriseMock();
        capture = extension.getBuildScan().getCapture();

        DevelocityAdapter extensionAdapter = DevelocityAdapter.create(extension);
        buildScan = extensionAdapter.getBuildScan();
        adapter = buildScan.getCapture();
    }

    @Test
    @DisplayName("file fingerprint capturing can be set via an adapter")
    void testFileFingerprints() {
        // when
        adapter.setFileFingerprints(true);

        // then
        verify(capture).setTaskInputFiles(true);

        // when
        adapter.isFileFingerprints();

        // then
        verify(capture).isTaskInputFiles();
    }

    @Test
    @DisplayName("build logging capturing can be set via an adapter")
    void testBuildLogging() {
        // when
        adapter.setBuildLogging(true);

        // then
        verify(capture).setBuildLogging(true);

        // when
        adapter.isBuildLogging();

        // then
        verify(capture).isBuildLogging();
    }

    @Test
    @DisplayName("test logging capturing can be set via an adapter")
    void testTestLogging() {
        // when
        adapter.setTestLogging(true);

        // then
        verify(capture).setTestLogging(true);

        // when
        adapter.isTestLogging();

        // then
        verify(capture).isTestLogging();
    }

    @Test
    @DisplayName("multiple obfuscation settings can be configured via an adapter")
    void testMultipleObfuscationSettings() {
        // when
        buildScan.capture(c -> {
            c.setTestLogging(true);
            c.setBuildLogging(true);
        });

        // then
        verify(capture).setTestLogging(true);
        verify(capture).setBuildLogging(true);
    }
}

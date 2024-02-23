package com.gradle.ccud.adapters.develocity;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.BuildScanCaptureAdapter;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.develocity.agent.gradle.DevelocityConfiguration;
import com.gradle.develocity.agent.gradle.scan.BuildScanCaptureConfiguration;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuildScanCaptureConfigurationAdapterTest extends BaseAdapterTest {

    private DevelocityAdapter develocity;
    private BuildScanCaptureConfiguration capture;
    private BuildScanCaptureAdapter adapter;

    @BeforeEach
    void setup() {
        DevelocityConfiguration develocityConfiguration = createDevelocityMock();
        develocity = DevelocityAdapter.create(develocityConfiguration);

        capture = develocityConfiguration.getBuildScan().getCapture();
        adapter = develocity.getBuildScan().getCapture();
    }

    @Test
    @DisplayName("file fingerprint capturing can be set via an adapter")
    void testFileFingerprints() {
        //given
        Property<Boolean> captureProp = mockPropertyReturning(false);
        when(capture.getFileFingerprints()).thenReturn(captureProp);

        // when
        adapter.setFileFingerprints(false);

        // then
        verify(capture.getFileFingerprints()).set(false);

        // when
        adapter.isFileFingerprints();

        // then
        verify(capture.getFileFingerprints()).get();
    }

    @Test
    @DisplayName("build logging capturing can be set via an adapter")
    void testBuildLogging() {
        //given
        Property<Boolean> captureProp = mockPropertyReturning(false);
        when(capture.getBuildLogging()).thenReturn(captureProp);

        // when
        adapter.setBuildLogging(false);

        // then
        verify(capture.getBuildLogging()).set(false);

        // when
        adapter.isBuildLogging();

        // then
        verify(capture.getBuildLogging()).get();
    }

    @Test
    @DisplayName("test logging capturing can be set via an adapter")
    void testTestLogging() {
        //given
        Property<Boolean> captureProp = mockPropertyReturning(false);
        when(capture.getTestLogging()).thenReturn(captureProp);

        // when
        adapter.setTestLogging(false);

        // then
        verify(capture.getTestLogging()).set(false);

        // when
        adapter.isTestLogging();

        // then
        verify(capture.getTestLogging()).get();
    }

    @Test
    @DisplayName("multiple obfuscation settings can be configured via an adapter")
    void testMultipleObfuscationSettings() {
        // given
        Property<Boolean> testLoggingProp = mockPropertyReturning(false);
        Property<Boolean> buildLoggingProp = mockPropertyReturning(false);
        when(capture.getTestLogging()).thenReturn(testLoggingProp);
        when(capture.getBuildLogging()).thenReturn(buildLoggingProp);

        // when
        develocity.getBuildScan().capture(c -> {
            c.setTestLogging(false);
            c.setBuildLogging(false);
        });

        // then
        verify(capture.getTestLogging()).set(false);
        verify(capture.getBuildLogging()).set(false);
    }
}

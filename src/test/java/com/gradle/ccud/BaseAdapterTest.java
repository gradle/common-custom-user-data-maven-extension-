package com.gradle.ccud;

import com.gradle.develocity.agent.gradle.DevelocityConfiguration;
import com.gradle.develocity.agent.gradle.scan.BuildScanCaptureConfiguration;
import com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration;
import com.gradle.develocity.agent.gradle.scan.BuildScanDataObfuscationConfiguration;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildScanCaptureSettings;
import com.gradle.scan.plugin.BuildScanDataObfuscation;
import com.gradle.scan.plugin.BuildScanExtension;
import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Stubber;

import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public abstract class BaseAdapterTest {

    protected static GradleEnterpriseExtension createGradleEnterpriseMock() {
        BuildScanExtension buildScan = mock();
        lenient().when(buildScan.getObfuscation()).thenReturn(mock(BuildScanDataObfuscation.class));
        lenient().when(buildScan.getCapture()).thenReturn(mock(BuildScanCaptureSettings.class));
        GradleEnterpriseExtension extension = mock();
        lenient().when(extension.getBuildScan()).thenReturn(buildScan);
        return extension;
    }

    protected static DevelocityConfiguration createDevelocityMock() {
        BuildScanConfiguration buildScan = mock();
        lenient().when(buildScan.getObfuscation()).thenReturn(mock(BuildScanDataObfuscationConfiguration.class));
        lenient().when(buildScan.getCapture()).thenReturn(mock(BuildScanCaptureConfiguration.class));
        DevelocityConfiguration configuration = mock();
        lenient().when(configuration.getBuildScan()).thenReturn(buildScan);
        return configuration;
    }

    @SuppressWarnings("unchecked")
    protected static <T> Property<T> mockPropertyReturning(T value) {
        Property<T> prop = (Property<T>) mock(Property.class);
        lenient().when(prop.get()).thenReturn(value);
        lenient().when(prop.getOrNull()).thenReturn(value);
        return prop;
    }

    protected static <T> Stubber doExecuteActionWith(T obj) {
        return doAnswer(invocation -> {
            Action<? super T> action = invocation.getArgument(0);
            action.execute(obj);
            return null;
        });
    }

    public static class ArgCapturingAction<T> implements Action<T> {

        private final AtomicReference<T> arg = new AtomicReference<>();

        @Override
        public void execute(T t) {
            arg.set(t);
        }

        public T getValue() {
            return arg.get();
        }
    }
}

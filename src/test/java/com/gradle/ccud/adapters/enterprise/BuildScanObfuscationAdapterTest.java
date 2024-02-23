package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.BaseAdapterTest;
import com.gradle.ccud.adapters.BuildScanAdapter;
import com.gradle.ccud.adapters.BuildScanObfuscationAdapter;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildScanDataObfuscation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BuildScanObfuscationAdapterTest extends BaseAdapterTest {

    private BuildScanAdapter buildScan;
    private BuildScanDataObfuscation obfuscation;
    private BuildScanObfuscationAdapter adapter;

    @BeforeEach
    void setup() {
        GradleEnterpriseExtension gradleEnterprise = createGradleEnterpriseMock();

        obfuscation = gradleEnterprise.getBuildScan().getObfuscation();

        buildScan = DevelocityAdapter.create(gradleEnterprise).getBuildScan();
        adapter = buildScan.getObfuscation();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("username obfuscation can be set via an adapter")
    void testUsername() {
        // when
        adapter.username(mock(Function.class));

        // then
        verify(obfuscation).username(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("hostname obfuscation can be set via an adapter")
    void testHostname() {
        // when
        adapter.hostname(mock(Function.class));

        // then
        verify(obfuscation).hostname(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("ip address obfuscation can be set via an adapter")
    void testIpAddresses() {
        // when
        adapter.ipAddresses(mock(Function.class));

        // then
        verify(obfuscation).ipAddresses(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("multiple obfuscation settings can be configured via an adapter")
    void testMultipleObfuscationSettings() {
        // when
        buildScan.obfuscation(o -> {
            o.username(mock(Function.class));
            o.hostname(mock(Function.class));
        });

        // then
        verify(obfuscation).username(any());
        verify(obfuscation).hostname(any());
    }
}

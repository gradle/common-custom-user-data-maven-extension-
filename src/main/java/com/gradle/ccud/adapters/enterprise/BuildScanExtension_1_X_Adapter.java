package com.gradle.ccud.adapters.enterprise;

import com.gradle.CommonCustomUserDataGradlePlugin;
import com.gradle.ccud.adapters.BuildScanAdapter;
import com.gradle.ccud.adapters.BuildScanCaptureAdapter;
import com.gradle.ccud.adapters.BuildScanObfuscationAdapter;
import com.gradle.ccud.adapters.DevelocityAdapter;
import com.gradle.ccud.adapters.shared.BuildResultAdapter;
import com.gradle.ccud.adapters.shared.PublishedBuildScanAdapter;
import com.gradle.ccud.proxies.ProxyFactory;
import com.gradle.scan.plugin.BuildScanExtension;
import org.gradle.api.Action;
import org.gradle.caching.configuration.AbstractBuildCache;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * For Gradle plugins 1.x, the `buildScan` extension is also the root extension.
 */
public class BuildScanExtension_1_X_Adapter implements DevelocityAdapter, BuildScanAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(CommonCustomUserDataGradlePlugin.class);

    private final BuildScanExtension extension;

    public BuildScanExtension_1_X_Adapter(BuildScanExtension extension) {
        this.extension = extension;
    }

    @Override
    public BuildScanAdapter getBuildScan() {
        return this;
    }

    @Override
    public void buildScan(Action<? super BuildScanAdapter> action) {
        action.execute(this);
    }

    @Override
    public void setServer(@Nullable String server) {
        extension.setServer(server);
    }

    @Nullable
    @Override
    public String getServer() {
        warnAboutUnsupportedOperation("getServer()");
        return null;
    }

    @Override
    public void setProjectId(@Nullable String projectId) {
        warnAboutUnsupportedOperation("setProjectId(String)");
    }

    @Nullable
    @Override
    public String getProjectId() {
        warnAboutUnsupportedOperation("getProjectId()");
        return null;
    }

    @Override
    public void setAllowUntrustedServer(boolean allow) {
        extension.setAllowUntrustedServer(allow);
    }

    @Override
    public boolean getAllowUntrustedServer() {
        warnAboutUnsupportedOperation("getAllowUntrustedServer()");
        return false;
    }

    @Override
    public void setAccessKey(@Nullable String accessKey) {
        warnAboutUnsupportedOperation("setAccessKey()");
    }

    @Nullable
    @Override
    public String getAccessKey() {
        warnAboutUnsupportedOperation("getAccessKey()");
        return null;
    }

    @Override
    public Class<? extends AbstractBuildCache> getBuildCache() {
        warnAboutUnsupportedOperation("getBuildCache()");
        return null;
    }

    @Override
    public void background(Action<? super BuildScanAdapter> action) {
        extension.background(__ -> action.execute(this));
    }

    @Override
    public void tag(String tag) {
        extension.tag(tag);
    }

    @Override
    public void value(String name, String value) {
        extension.value(name, value);
    }

    @Override
    public void link(String name, String url) {
        extension.link(name, url);
    }

    @Override
    public void buildFinished(Action<? super BuildResultAdapter> action) {
        //noinspection Convert2Lambda
        extension.buildFinished(buildResult -> action.execute(new BuildResultAdapter() {
            @Override
            public List<Throwable> getFailures() {
                return Collections.singletonList(buildResult.getFailure());
            }
        }));
    }

    @Override
    public void buildScanPublished(Action<? super PublishedBuildScanAdapter> action) {
        extension.buildScanPublished(scan -> action.execute(ProxyFactory.createProxy(scan, PublishedBuildScanAdapter.class)));
    }

    @Override
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        extension.setTermsOfServiceUrl(termsOfServiceUrl);
    }

    @Nullable
    @Override
    public String getTermsOfServiceUrl() {
        warnAboutUnsupportedOperation("getTermsOfServiceUrl()");
        return null;
    }

    @Override
    public void setTermsOfServiceAgree(@Nullable String agree) {
        extension.setTermsOfServiceAgree(agree);
    }

    @Nullable
    @Override
    public String getTermsOfServiceAgree() {
        warnAboutUnsupportedOperation("getTermsOfServiceAgree()");
        return null;
    }

    @Override
    public void setUploadInBackground(boolean uploadInBackground) {
        warnAboutUnsupportedOperation("setUploadInBackground(boolean)");
    }

    @Override
    public boolean isUploadInBackground() {
        warnAboutUnsupportedOperation("isUploadInBackground()");
        return false;
    }

    @Override
    public void publishAlways() {
        extension.publishAlways();
    }

    @Override
    public void publishAlwaysIf(boolean condition) {
        extension.publishAlwaysIf(condition);
    }

    @Override
    public void publishOnFailure() {
        extension.publishOnFailure();
    }

    @Override
    public void publishOnFailureIf(boolean condition) {
        extension.publishOnFailureIf(condition);
    }

    @Override
    public BuildScanObfuscationAdapter getObfuscation() {
        warnAboutUnsupportedOperation("getObfuscation()");
        return null;
    }

    @Override
    public void obfuscation(Action<? super BuildScanObfuscationAdapter> action) {
        warnAboutUnsupportedOperation("obfuscation(Action)");
    }

    @Override
    public BuildScanCaptureAdapter getCapture() {
        warnAboutUnsupportedOperation("getCapture()");
        return null;
    }

    @Override
    public void capture(Action<? super BuildScanCaptureAdapter> action) {
        warnAboutUnsupportedOperation("capture(Action)");
    }

    private static void warnAboutUnsupportedOperation(String op) {
        LOG.warn("Build Scan plugin version 1.x does not support '" + op + "' operation");
    }
}

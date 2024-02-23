package com.gradle.ccud.adapters.develocity;

import com.gradle.ccud.adapters.BuildScanAdapter;
import com.gradle.ccud.adapters.BuildScanCaptureAdapter;
import com.gradle.ccud.adapters.BuildScanObfuscationAdapter;
import com.gradle.ccud.adapters.shared.BuildResultAdapter;
import com.gradle.ccud.adapters.shared.PublishedBuildScanAdapter;
import com.gradle.ccud.proxies.ProxyFactory;
import com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration;
import org.gradle.api.Action;
import org.jetbrains.annotations.Nullable;

class BuildScanConfigurationAdapter implements BuildScanAdapter {

    private final BuildScanConfiguration buildScan;
    private final BuildScanObfuscationAdapter obfuscation;
    private final BuildScanCaptureAdapter capture;

    BuildScanConfigurationAdapter(BuildScanConfiguration buildScan) {
        this.buildScan = buildScan;
        this.obfuscation = new BuildScanDataObfuscationConfigurationAdapter(buildScan.getObfuscation());
        this.capture = new BuildScanCaptureConfigurationAdapter(buildScan.getCapture());
    }

    @Override
    public void background(Action<? super BuildScanAdapter> action) {
        buildScan.background(__ -> action.execute(this));
    }

    @Override
    public void tag(String tag) {
        buildScan.tag(tag);
    }

    @Override
    public void value(String name, String value) {
        buildScan.value(name, value);
    }

    @Override
    public void link(String name, String url) {
        buildScan.link(name, url);
    }

    @Override
    public void buildFinished(Action<? super BuildResultAdapter> action) {
        buildScan.buildFinished(buildResult -> action.execute(ProxyFactory.createProxy(buildResult, BuildResultAdapter.class)));
    }

    @Override
    public void buildScanPublished(Action<? super PublishedBuildScanAdapter> action) {
        buildScan.buildScanPublished(scan -> action.execute(ProxyFactory.createProxy(scan, PublishedBuildScanAdapter.class)));
    }

    @Override
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        buildScan.getTermsOfServiceUrl().set(termsOfServiceUrl);
    }

    @Nullable
    @Override
    public String getTermsOfServiceUrl() {
        return buildScan.getTermsOfServiceUrl().getOrNull();
    }

    @Override
    public void setTermsOfServiceAgree(@Nullable String agree) {
        buildScan.getTermsOfServiceAgree().set(agree);
    }

    @Nullable
    @Override
    public String getTermsOfServiceAgree() {
        return buildScan.getTermsOfServiceAgree().getOrNull();
    }

    @Override
    public void setUploadInBackground(boolean uploadInBackground) {
        buildScan.getUploadInBackground().set(uploadInBackground);
    }

    @Override
    public boolean isUploadInBackground() {
        return buildScan.getUploadInBackground().get();
    }

    @Override
    public void publishAlways() {
        buildScan.publishing(publishing -> publishing.onlyIf(ctx -> true));
    }

    @Override
    public void publishAlwaysIf(boolean condition) {
        buildScan.publishing(publishing -> publishing.onlyIf(ctx -> condition));
    }

    @Override
    public void publishOnFailure() {
        buildScan.publishing(publishing -> publishing.onlyIf(ctx -> !ctx.getBuildResult().getFailures().isEmpty()));
    }

    @Override
    public void publishOnFailureIf(boolean condition) {
        buildScan.publishing(publishing -> publishing.onlyIf(ctx -> !ctx.getBuildResult().getFailures().isEmpty() && condition));
    }

    @Override
    public BuildScanObfuscationAdapter getObfuscation() {
        return obfuscation;
    }

    @Override
    public void obfuscation(Action<? super BuildScanObfuscationAdapter> action) {
        action.execute(obfuscation);
    }

    @Override
    public BuildScanCaptureAdapter getCapture() {
        return capture;
    }

    @Override
    public void capture(Action<? super BuildScanCaptureAdapter> action) {
        action.execute(capture);
    }
}

package com.gradle.ccud.adapters;

import com.gradle.ccud.adapters.shared.BuildResultAdapter;
import com.gradle.ccud.adapters.shared.PublishedBuildScanAdapter;
import org.gradle.api.Action;

import javax.annotation.Nullable;

public interface BuildScanAdapter {

    void background(Action<? super BuildScanAdapter> action);

    void tag(String tag);

    void value(String name, String value);

    void link(String name, String url);

    void buildFinished(Action<? super BuildResultAdapter> action);

    void buildScanPublished(Action<? super PublishedBuildScanAdapter> action);

    void setTermsOfServiceUrl(String termsOfServiceUrl);

    @Nullable
    String getTermsOfServiceUrl();

    void setTermsOfServiceAgree(@Nullable String agree);

    @Nullable
    String getTermsOfServiceAgree();

    void setUploadInBackground(boolean uploadInBackground);

    boolean isUploadInBackground();

    void publishAlways();

    void publishAlwaysIf(boolean condition);

    void publishOnFailure();

    void publishOnFailureIf(boolean condition);

    @Nullable
    BuildScanObfuscationAdapter getObfuscation();

    void obfuscation(Action<? super BuildScanObfuscationAdapter> action);

    @Nullable
    BuildScanCaptureAdapter getCapture();

    void capture(Action<? super BuildScanCaptureAdapter> action);
}

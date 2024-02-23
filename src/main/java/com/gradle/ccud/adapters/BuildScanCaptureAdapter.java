package com.gradle.ccud.adapters;

import com.gradle.develocity.agent.gradle.scan.BuildScanCaptureConfiguration;
import com.gradle.scan.plugin.BuildScanCaptureSettings;

public interface BuildScanCaptureAdapter {

    /**
     * @see BuildScanCaptureConfiguration#getFileFingerprints()
     * @see BuildScanCaptureSettings#setTaskInputFiles(boolean)
     */
    void setFileFingerprints(boolean capture);

    /**
     * @see BuildScanCaptureConfiguration#getFileFingerprints()
     * @see BuildScanCaptureSettings#isTaskInputFiles()
     */
    boolean isFileFingerprints();

    /**
     * @see BuildScanCaptureConfiguration#getBuildLogging()
     * @see BuildScanCaptureSettings#setBuildLogging(boolean)
     */
    void setBuildLogging(boolean capture);

    /**
     * @see BuildScanCaptureConfiguration#getBuildLogging()
     * @see BuildScanCaptureSettings#isBuildLogging()
     */
    boolean isBuildLogging();

    /**
     * @see BuildScanCaptureConfiguration#getTestLogging()
     * @see BuildScanCaptureSettings#setTestLogging(boolean)
     */
    void setTestLogging(boolean capture);

    /**
     * @see BuildScanCaptureConfiguration#getTestLogging()
     * @see BuildScanCaptureSettings#isTestLogging()
     */
    boolean isTestLogging();

}

package com.gradle.ccud.adapters;

import com.gradle.ccud.adapters.develocity.DevelocityConfigurationAdapter;
import com.gradle.ccud.adapters.enterprise.BuildScanExtension_1_X_Adapter;
import com.gradle.ccud.adapters.enterprise.GradleEnterpriseExtensionAdapter;
import com.gradle.ccud.proxies.ProxyFactory;
import com.gradle.ccud.proxies.enterprise.GradleEnterpriseExtensionProxy;
import com.gradle.develocity.agent.gradle.DevelocityConfiguration;
import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.scan.plugin.BuildScanExtension;
import org.gradle.api.Action;
import org.gradle.caching.configuration.AbstractBuildCache;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.gradle.Utils.isGradle5OrNewer;

public interface DevelocityAdapter {

    String DEVELOCITY_CONFIGURATION = "com.gradle.develocity.agent.gradle.DevelocityConfiguration";
    String GRADLE_ENTERPRISE_EXTENSION = "com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension";
    String BUILD_SCAN_EXTENSION = "com.gradle.scan.plugin.BuildScanExtension";

    static DevelocityAdapter create(Object gradleEnterpriseOrDevelocity) {
        if (isDevelocityConfiguration(gradleEnterpriseOrDevelocity)) {
            return new DevelocityConfigurationAdapter(ProxyFactory.createProxy(gradleEnterpriseOrDevelocity, DevelocityConfiguration.class));
        } else if (isGradleEnterpriseExtension(gradleEnterpriseOrDevelocity)) {
            return new GradleEnterpriseExtensionAdapter(ProxyFactory.createProxy(gradleEnterpriseOrDevelocity, GradleEnterpriseExtensionProxy.class));
        } else if (!isGradle5OrNewer() && isBuildScanExtension(gradleEnterpriseOrDevelocity)) {
            // Build Scan plugin only exposes the buildScan extension with a limited functionality
            return new BuildScanExtension_1_X_Adapter(ProxyFactory.createProxy(gradleEnterpriseOrDevelocity, BuildScanExtension.class));
        }

        throw new IllegalArgumentException("Provided extension of type '" + gradleEnterpriseOrDevelocity.getClass().getName() + "' is neither the Develocity configuration nor the Gradle Enterprise extension");
    }

    static boolean isDevelocityConfiguration(Object gradleEnterpriseOrDevelocity) {
        return implementsInterface(gradleEnterpriseOrDevelocity, DEVELOCITY_CONFIGURATION);
    }

    static boolean isGradleEnterpriseExtension(Object gradleEnterpriseOrDevelocity) {
        return implementsInterface(gradleEnterpriseOrDevelocity, GRADLE_ENTERPRISE_EXTENSION);
    }

    static boolean isBuildScanExtension(Object gradleEnterpriseOrDevelocity) {
        return implementsInterface(gradleEnterpriseOrDevelocity, BUILD_SCAN_EXTENSION);
    }

    static boolean implementsInterface(Object object, String interfaceName) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            boolean implementsInterface = Stream.concat(
                Arrays.stream(interfaces),
                Arrays.stream(interfaces).flatMap(it -> Arrays.stream(it.getInterfaces()))
            ).anyMatch(it -> interfaceName.equals(it.getName()));

            if (implementsInterface) {
                return true;
            }

            clazz = clazz.getSuperclass();
        }

        return false;
    }

    /**
     * @see DevelocityConfiguration#getBuildScan()
     * @see GradleEnterpriseExtension#getBuildScan()
     */
    BuildScanAdapter getBuildScan();

    /**
     * @see DevelocityConfiguration#buildScan(Action)
     * @see GradleEnterpriseExtension#buildScan(Action)
     */
    void buildScan(Action<? super BuildScanAdapter> action);

    /**
     * @see DevelocityConfiguration#getServer()
     * @see GradleEnterpriseExtension#setServer(String)
     */
    void setServer(@Nullable String server);

    /**
     * @see DevelocityConfiguration#getServer()
     * @see GradleEnterpriseExtension#getServer()
     */
    @Nullable
    String getServer();

    /**
     * @see DevelocityConfiguration#getProjectId() ()
     * @see GradleEnterpriseExtension#setProjectId(String)
     */
    void setProjectId(@Nullable String projectId);

    /**
     * @see DevelocityConfiguration#getProjectId() ()
     * @see GradleEnterpriseExtension#getProjectId()
     */
    @Nullable
    String getProjectId();

    /**
     * @see DevelocityConfiguration#getAllowUntrustedServer()
     * @see GradleEnterpriseExtension#setAllowUntrustedServer(boolean)
     */
    void setAllowUntrustedServer(boolean allow);

    /**
     * @see DevelocityConfiguration#getAllowUntrustedServer()
     * @see GradleEnterpriseExtension#getAllowUntrustedServer()
     */
    boolean getAllowUntrustedServer();

    /**
     * @see DevelocityConfiguration#getAccessKey()
     * @see GradleEnterpriseExtension#setAccessKey(String)
     */
    void setAccessKey(@Nullable String accessKey);

    /**
     * @see DevelocityConfiguration#getAccessKey()
     * @see GradleEnterpriseExtension#getAccessKey()
     */
    @Nullable
    String getAccessKey();

    /**
     * @see DevelocityConfiguration#getBuildCache()
     * @see GradleEnterpriseExtension#getBuildCache()
     */
    @Nullable
    Class<? extends AbstractBuildCache> getBuildCache();

}

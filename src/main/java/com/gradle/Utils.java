package com.gradle;

import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.GradleVersion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.lang.Boolean.parseBoolean;

final class Utils {

    static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    static Optional<String> sysProperty(String name) {
        return Optional.ofNullable(System.getProperty(name));
    }

    static boolean sysPropertyKeyStartingWith(String keyPrefix) {
        for (Object key : System.getProperties().keySet()) {
            if (key instanceof String) {
                String stringKey = (String) key;
                if (stringKey.startsWith(keyPrefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    static void withSysProperty(String name, Consumer<String> action, ProviderFactory providers) {
        if (isGradle65OrNewer()) {
            Provider<String> property = providers.systemProperty(name).forUseAtConfigurationTime();
            if (property.isPresent()) {
                action.accept(property.get());
            }
        } else {
            Optional<String> property = sysProperty(name);
            property.ifPresent(action);
        }
    }

    static void withBooleanSysProperty(String name, Consumer<Boolean> action, ProviderFactory providers) {
        withSysProperty(name, value -> action.accept(parseBoolean(value)), providers);
    }

    static void withDurationSysProperty(String name, Consumer<Duration> action, ProviderFactory providers) {
        withSysProperty(name, value -> action.accept(Duration.parse(value)), providers);
    }

    static Optional<String> envVariable(String name) {
        String value = System.getenv(name);
        if (isNotEmpty(value)) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    static String appendIfMissing(String str, String suffix) {
        return str.endsWith(suffix) ? str : str + suffix;
    }

    static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static Properties readPropertiesFile(String name) {
        try (InputStream input = new FileInputStream(name)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean execAndCheckSuccess(String... args) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(args);
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            return finished && process.exitValue() == 0;
        } catch (IOException | InterruptedException ignored) {
            return false;
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    static String execAndGetStdOut(String... args) {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Reader standard = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
            try (Reader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.defaultCharset()))) {
                String standardText = readFully(standard);
                String ignore = readFully(error);

                boolean finished = process.waitFor(10, TimeUnit.SECONDS);
                return finished && process.exitValue() == 0 ? trimAtEnd(standardText) : null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            process.destroyForcibly();
        }
    }

    private static String readFully(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int nRead;
        while ((nRead = reader.read(buf)) != -1) {
            sb.append(buf, 0, nRead);
        }
        return sb.toString();
    }

    private static String trimAtEnd(String str) {
        return ('x' + str).trim().substring(1);
    }

    private static boolean isGradle65OrNewer() {
        return GradleVersion.current().compareTo(GradleVersion.version("6.5")) >= 0;
    }

    private Utils() {
    }

}

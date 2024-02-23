package com.gradle.ccud.adapters;

import com.gradle.develocity.agent.gradle.scan.BuildScanDataObfuscationConfiguration;
import com.gradle.scan.plugin.BuildScanDataObfuscation;

import java.net.InetAddress;
import java.util.List;
import java.util.function.Function;

public interface BuildScanObfuscationAdapter {

    /**
     * @see BuildScanDataObfuscationConfiguration#username(Function)
     * @see BuildScanDataObfuscation#username(Function)
     */
    void username(Function<? super String, ? extends String> obfuscator);

    /**
     * @see BuildScanDataObfuscationConfiguration#hostname(Function)
     * @see BuildScanDataObfuscation#hostname(Function)
     */
    void hostname(Function<? super String, ? extends String> obfuscator);

    /**
     * @see BuildScanDataObfuscationConfiguration#ipAddresses(Function)
     * @see BuildScanDataObfuscation#ipAddresses(Function)
     */
    void ipAddresses(Function<? super List<InetAddress>, ? extends List<String>> obfuscator);
}

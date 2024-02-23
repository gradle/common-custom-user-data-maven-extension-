package com.gradle.ccud.adapters.enterprise;

import com.gradle.ccud.proxies.enterprise.BuildScanDataObfuscationProxy;

import java.net.InetAddress;
import java.util.List;
import java.util.function.Function;

class BuildScanDataObfuscationAdapter implements com.gradle.ccud.adapters.BuildScanObfuscationAdapter {

    private final BuildScanDataObfuscationProxy obfuscation;

    BuildScanDataObfuscationAdapter(BuildScanDataObfuscationProxy obfuscation) {
        this.obfuscation = obfuscation;
    }

    @Override
    public void username(Function<? super String, ? extends String> obfuscator) {
        obfuscation.username(obfuscator);
    }

    @Override
    public void hostname(Function<? super String, ? extends String> obfuscator) {
        obfuscation.hostname(obfuscator);
    }

    @Override
    public void ipAddresses(Function<? super List<InetAddress>, ? extends List<String>> obfuscator) {
        obfuscation.ipAddresses(obfuscator);
    }
}

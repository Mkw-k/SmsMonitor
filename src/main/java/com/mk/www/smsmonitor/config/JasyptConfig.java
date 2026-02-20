package com.mk.www.smsmonitor.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    private static final String KEY_PATH = "data/important/jasyp_key";

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        try {
            // /data/important/jasyp_key 파일에서 첫 줄을 읽어와 비밀키로 사용
            String password = Files.readAllLines(Paths.get(KEY_PATH)).get(0).trim();
            config.setPassword(password);
        } catch (IOException | IndexOutOfBoundsException e) {
            log.error("Jasypt Key File not found or empty at {}", KEY_PATH);
            // 키 파일이 없을 경우 애플리케이션 구동을 중단하거나 기본 동작을 정의할 수 있습니다.
            throw new RuntimeException("Required Jasypt key file is missing at " + KEY_PATH);
        }

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        
        return encryptor;
    }
}

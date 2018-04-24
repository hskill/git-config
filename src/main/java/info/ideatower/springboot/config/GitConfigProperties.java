package info.ideatower.springboot.config;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * git配置参数
 *
 * <code>
 *
 *     git-config:
 *       enabled: on
 *       uri: https://github.com/hskill/git-config.git
 *       username: ~
 *       password: ~
 *       files: application.yml
 *       privateKey: ~
 * </code>
 */
@Slf4j
@Data
@ConfigurationProperties("git-config")
public class GitConfigProperties {

    /** 是否启用 */
    private boolean enabled = true;

    /** username for git server */
    private String username;

    /** password for git server */
    private String password;

    /** uri for git server */
    private String uri;

    /** for ssh access */
    private String privateKey;

    /** 文件列表 */
    private List<String> files = Lists.newArrayList();

    public static GitConfigProperties override(Environment environment) {
        GitConfigProperties properties = new GitConfigProperties();
        properties.enabled = environment.getProperty("git-config.enabled", Boolean.class, true);
        log.debug("git-config.enabled: {}", properties.enabled);
        properties.uri = environment.getProperty("git-config.uri");
        log.debug("git-config.uri: {}", properties.uri);
        if (environment.containsProperty("git-config.username")) {
            properties.username = environment.getProperty("git-config.username");
            log.debug("git-config.username: {}", properties.username);
        }
        if (environment.containsProperty("git-config.password")) {
            properties.password = environment.getProperty("git-config.password");
            log.debug("git-config.password", properties.password);
        }
        if (environment.containsProperty("git-config.files")) {
            properties.files = Lists.newArrayList(environment.getProperty("git-config.files", String[].class, new String[] {}));
            log.debug("git-config.files: {}", properties.files);
        }
        if (environment.containsProperty("git-config.privateKey")) {
            properties.privateKey = environment.getProperty("git-config.privateKey", StringUtils.EMPTY);
            log.debug("git-config.privateKey: {}", properties.privateKey);
        }
        return properties;
    }
}

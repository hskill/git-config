package info.ideatower.springboot.config;

import info.ideatower.springboot.config.git.GitConfigEnvironmentLocator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Environment处理
 */
@Slf4j
@Component
public class GitConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Autowired
    private GitConfigProperties properties;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        this.properties = GitConfigProperties.override(environment);

        if (environment.containsProperty("git-config.uri") && this.properties.isEnabled()) {
            log.debug("load config from {}", this.properties.getUri());

            PropertySource<Map<String, Object>> propertySource
                    = new MapPropertySource("gitEnvironment:[" + properties.getUri() + "]", getGitEnvironment());
            environment.getPropertySources().addLast(propertySource);
        }
    }

    protected Map<String, Object> getGitEnvironment() {
        val locator = new GitConfigEnvironmentLocator();
        return locator.locate(this.properties);
    }
}

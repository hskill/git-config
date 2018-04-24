package info.ideatower.springboot.config.git;

import info.ideatower.springboot.config.GitConfigProperties;
import lombok.Data;

/**
 *
 * Git访问的认证信息
 */
@Data
public class Credential {

    private String username;
    private String password;

    public Credential(GitConfigProperties properties) {
        this.username = properties.getUsername();
        this.password = properties.getPassword();
    }
}
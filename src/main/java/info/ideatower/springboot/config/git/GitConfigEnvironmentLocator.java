package info.ideatower.springboot.config.git;

import info.ideatower.springboot.config.GitConfigProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.Map;

/**
 * Git Config 文件获取
 */
@Slf4j
public class GitConfigEnvironmentLocator {


    public GitConfigEnvironmentLocator() {
    }

    /**
     * 定位配置Map
     * @param properties
     * @return
     */
    public Map<String, Object> locate(GitConfigProperties properties) {

        // 将git文件下载到临时文件目录
        val localpath = System.getProperty("java.io.tmpdir")
                + "git-config"
                + File.separator
                + DigestUtils.md5Hex(properties.getUri());

        // 准备文件
        val git = new GitRepository(properties, localpath);
        return git.read();

    }

}

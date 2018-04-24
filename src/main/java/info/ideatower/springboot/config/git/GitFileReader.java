package info.ideatower.springboot.config.git;

import com.google.common.collect.Maps;
import info.ideatower.springboot.config.GitConfigProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class GitFileReader {

    private final GitConfigProperties properties;
    private final String localpath;

    public GitFileReader(GitConfigProperties properties, String localpath) {
        this.properties = properties;
        this.localpath = localpath;
    }

    /**
     * 读取文件
     * @return
     */
    public Map<String,Object> read() {
        val path = localpath + File.separator;
        Map<String, Object> result = Maps.newHashMap();
        for (val filePath : properties.getFiles()) {
            val map = readSingleFile(path + filePath);
            result.putAll(map);
        }
        return result;
    }

    /**
     * 读取单个文件
     * @param filePath
     * @return
     */
    protected Map<String, Object> readSingleFile(String filePath) {
        Map<String, Object> result = Maps.newHashMap();
        val configFile = new File(filePath);
        if (!configFile.exists() || !configFile.canRead()) {
            log.error("{} 文件不存在或不能读取", filePath);
            throw new RuntimeException(String.format("%s 文件不存在或不能读取：%s", filePath));
        }

        try {
            val source = (new Yaml()).loadAs(new FileInputStream(filePath), Map.class);
            buildFlattenedMap(result, source, null);
        } catch (FileNotFoundException e) {
            log.error("读取sprinboot git config出错：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 将层级结构的Map扁平化。从spring-cloud-config 拷贝而来
     * @param result
     * @param source
     * @param path
     */
    private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.isNotBlank(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                }
                else {
                    key = path + '.' + key;
                }
            }

            Object value = entry.getValue();
            if (value instanceof String) {
                result.put(key, value);
            }
            else if (value instanceof Map) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, key);
            }
            else if (value instanceof Collection) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) value;
                int count = 0;
                for (Object object : collection) {
                    buildFlattenedMap(result,
                            Collections.singletonMap("[" + (count++) + "]", object), key);
                }
            }
            else {
                result.put(key, (value != null ? value : ""));
            }
        }
    }
}

package info.ideatower.springboot.config.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import info.ideatower.springboot.config.GitConfigProperties;
import lombok.val;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.util.Map;

/**
 * 访问Git Repository
 */
public class GitRepository {

    private final GitConfigProperties properties;
    private final String localPath;

    public GitRepository(GitConfigProperties properties, String localPath) {
        this.properties = properties;
        this.localPath = localPath;
    }

    /**
     * 准备文件
     */
    protected void ready() {
        val file = new File(this.localPath);
        // 如果目录不存在，则创建文件夹，且clone
        if (!file.exists()) {
            this.get();
        }
        else {//pull
            this.pull();
        }
    }

    /**
     * clone for git repository
     */
    protected void get() {
        createCacheDir(this.localPath);

        val credential = new Credential(properties);
        val cc = Git.cloneRepository()
                .setURI(properties.getUri())
                .setBranch("master")
                .setDirectory(new File(this.localPath));

        if (this.properties.getUri().startsWith("git")) {

            final SshSessionFactory factory = new JschConfigSessionFactory() {
                @Override
                public void configure(OpenSshConfig.Host hc, com.jcraft.jsch.Session session) {
                    session.setConfig("StrictHostKeyChecking", "no");
                }

                @Override
                protected JSch getJSch(final OpenSshConfig.Host hc,
                                       org.eclipse.jgit.util.FS fs) throws JSchException {
                    JSch jsch = super.getJSch(hc, fs);
                    jsch.removeAllIdentity();

                    val privateKey = GitRepository.this.properties.getPrivateKey();

                    // 如果privateKey传入的是文件，则使用文件连接，否则用私钥字符串连接
                    val privateKeyFile = new File(privateKey);
                    if (privateKeyFile.exists()) {
                        jsch.addIdentity(privateKey);
                    }
                    else {
                        jsch.addIdentity("provided-key", privateKey.getBytes(), null, null);
                    }
                    return jsch;
                }
            };

            TransportConfigCallback setKeyCallback = new TransportConfigCallback() {
                @Override
                public void configure(Transport transport) {
                    if (transport instanceof SshTransport) {
                        SshTransport sshTransport = (SshTransport) transport;
                        sshTransport.setSshSessionFactory(factory);
                    }
                }
            };

        }
        else {

            cc.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                    credential.getUsername(),
                    credential.getPassword()
            ));
        }


        try {
            cc.call();
        } catch (GitAPIException e) {
            throw new RuntimeException("git clone时出错", e);
        }
    }

    /**
     * pull for git repository
     */
    protected void pull() {
        try {
            val builder = new FileRepositoryBuilder();
            val repository = builder.setGitDir(new File(this.localPath + File.separator + ".git"))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            val credential = new Credential(properties);

            val git = new Git(repository);
            val pullCmd = git.pull()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                            credential.getUsername(),
                            credential.getPassword()
                    ));

            pullCmd.call();

        } catch (Exception e) {
            throw new RuntimeException("git pull时出错", e);
        }
    }

    /**
     * 在临时目录中创建git的缓存文件夹
     *
     * @param localpath
     */
    protected void createCacheDir(String localpath) {
        val cacheDir = new File(localpath);
        if (cacheDir.isDirectory() && cacheDir.exists()) {
            return;
        }
        try {
            cacheDir.mkdirs();
        } catch (Exception e) {
            throw new RuntimeException("无法git创建缓存文件夹", e);
        }
    }

    /**
     * 读取所有配置文件
     * @return
     */
    public Map<String,Object> read() {
        this.ready();

        val reader = new GitFileReader(this.properties, this.localPath);
        return reader.read();
    }
}

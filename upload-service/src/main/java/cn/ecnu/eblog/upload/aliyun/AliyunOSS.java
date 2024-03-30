package cn.ecnu.eblog.upload.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@ConfigurationProperties(prefix = "aliyun.oss")
@Data
@Component
public class AliyunOSS {
    private String endpoint;
    private String bucketName;
    private EnvironmentVariableCredentialsProvider provider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

    public AliyunOSS() throws ClientException {}

    public String upload(MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = null;
        if (originalFilename != null) {
            fileName = UUID.randomUUID()+ originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        OSS ossClient = new OSSClientBuilder().build(endpoint, provider);
        ossClient.putObject(bucketName, fileName, input);

        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        ossClient.shutdown();
        return url;
    }

}

import com.test.TestApplication;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
public class TestEncrypt {

    @Autowired
    private StringEncryptor encryptor;
    @Value("${jasypt.encryptString}")
    private String encryptString;

    @Test
    public void mysqlEncrypt(){
        //每次加密root都不一样。但是解密后的值都一样。
        String root = encryptor.encrypt("yaojiaxing");
        System.out.println(root);
        String decrypt = encryptor.decrypt(root);
        System.out.println(decrypt);
        String decrypt1 = encryptor.decrypt("8lad8A7v1XBhHoR6m4+88A==");
        System.out.println(decrypt1);
        System.out.println(encryptString);
    }
}

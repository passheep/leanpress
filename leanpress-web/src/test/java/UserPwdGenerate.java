import com.jfinal.kit.HashKit;
import org.junit.Test;

/**
 * @author Passheep
 * @version V1.0
 * @Package PACKAGE_NAME
 */
public class UserPwdGenerate {

    @Test
    public void generate() {
        String pwd = "123456";
        String salt = HashKit.generateSaltForSha256();

        String hashedPwd = HashKit.sha256(salt + pwd);

        System.out.println("login pwd : " + pwd);
        System.out.println("salt : " + salt);
        System.out.println("hashed pwd : " + hashedPwd);
    }
}

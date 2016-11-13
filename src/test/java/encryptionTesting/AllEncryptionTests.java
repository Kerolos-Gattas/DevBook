package encryptionTesting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	EncryptionTesting.class,
	AuthenticationTests.class
})
public class AllEncryptionTests {

}

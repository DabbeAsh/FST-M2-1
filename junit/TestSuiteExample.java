import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;

@Suite
// With single package name
@SelectPackages("examples")
public class TestSuiteExample{}

package test.java;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommitListImplTest.class, GitCredTest.class,
        JsonApiClassTest.class, ProjectTest.class })
public class AllTests {

}

package org.jirafa.listener;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.jirafa.search.JiraSearcher;
import org.jirafa.search.SearchCriteria;
import org.jirafa.writer.Writer;
import org.jirafa.writer.XmlWriter;
import org.jirafa.writer.entity.Jira;
import org.jirafa.writer.entity.TestRun;
import org.jirafa.writer.entity.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hooking point for Jirafa to JUnit. Attach this listener to your JUnit tests and Jirafa will start working
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class JUnitListener extends RunListener {

    private Map<String, List<Failure>> failures = new HashMap<String, List<Failure>>();

    private static final String JIRA_BROWSE_URL = System.getProperty("jiraUrl") + "/browse/";

    @Override
    public void testFailure(Failure failure) throws Exception {
        String className = failure.getDescription().getClassName();
        if (!failures.containsKey(className)) {
            failures.put(className, new ArrayList<Failure>());
        }
        failures.get(className).add(failure);

        super.testFailure(failure);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        if (result.wasSuccessful()) {
            return;
        }

        JiraSearcher searcher = new JiraSearcher();
        searcher.connect();
        Writer writer = new XmlWriter();

        try {
            for (List<Failure> testFailures: failures.values()) {
                TestRun testRun = new TestRun();
                Description firstFailureDescription = testFailures.get(0).getDescription();
                testRun.setName(firstFailureDescription.getTestClass().getSimpleName());
                testRun.setPackageName(firstFailureDescription.getTestClass().getPackage().getName());

                List<Test> tests = new ArrayList<Test>();
                for (Failure failure : testFailures) {
                    Description description = failure.getDescription();

                    Test test = new Test();
                    test.setName(description.getMethodName());

                    SearchCriteria criteria = new SearchCriteria(description.getMethodName(), description.getTestClass().getSimpleName(), description.getTestClass().getPackage().getName());
                    List<Issue> foundIssues = searcher.search(criteria);

                    List<Jira> jiras = new ArrayList<Jira>();
                    for (Issue issue : foundIssues) {
                        Jira jira = new Jira();
                        jira.setKey(issue.getKey());
                        jira.setLink(JIRA_BROWSE_URL + issue.getKey());
                        jiras.add(jira);
                    }
                    test.setJiras(jiras);
                    tests.add(test);
                }

                testRun.setTests(tests);

                writer.write(testRun, testRun.getPackageName() + "." + testRun.getName());
            }
        } finally {
            searcher.close();
        }

        super.testRunFinished(result);
    }

}

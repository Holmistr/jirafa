package org.jirafa.search;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class performs search of the issues in configured JIRA.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class JiraSearcher {

    private static JiraRestClient client;

    private static final String JIRA_URL = System.getProperty("jiraUrl");
    private static final String FILTER = System.getProperty("filter");
    private static final String AUTH_USERNAME = System.getProperty("authUsername");
    private static final String AUTH_PASSWORD = System.getProperty("authPassword");
    private static final int MAX_RESULTS = Integer.valueOf(System.getProperty("maxResults", "5"));


    public List<Issue> search(SearchCriteria searchCriteria) {
        String fullName = searchCriteria.getPackageName() + "." + searchCriteria.getTestName() + "." + searchCriteria.getMethodName();
        String testName = searchCriteria.getTestName();
        String testWithMethodName = searchCriteria.getTestName() + "." + searchCriteria.getMethodName();

        String query = "(" + FILTER + ") AND (text ~ \"" +
                fullName + "\" OR text ~ \"" +
                testName + "\" OR text ~ \"" +
                testWithMethodName + "\")";

        SearchResult result = client.getSearchClient()
                    .searchJql(query, MAX_RESULTS, 0, null)
                    .claim();

        List<Issue> issues = new ArrayList<Issue>();
        for (Issue issue: result.getIssues()) {
            issues.add(issue);
        }
        return issues;
    }

    public void connect() {
        if (client == null) {
            JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

            URI jiraServerUri;
            try {
                jiraServerUri = new URI(JIRA_URL);
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Invalid URI syntax of JIRA server.", ex);
            }

            client = factory.createWithBasicHttpAuthentication(jiraServerUri, AUTH_USERNAME, AUTH_PASSWORD);
        }
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error occurred while closing the JIRA client.", ex);
            } finally {
                client = null;
            }
        }
    }

}

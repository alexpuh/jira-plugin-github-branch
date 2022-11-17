package de.alexpuh;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GitBranchContextProvider extends AbstractJiraContextProvider {

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");

        String branchName = prepareBranchName(currentIssue.getKey(), currentIssue.getIssueType().getName(), currentIssue.getSummary());
        contextMap.put("proposedBranchName", branchName);
        return contextMap;
    }

    private static String allowedSymbols = "§$&{}!()=#',";
    private String prepareBranchName(String issueKey, String issueType, String issueSummary) {

        StringBuilder sb = new StringBuilder();
        if ("Bug".equalsIgnoreCase(issueType)) {
            sb.append("bugfix/");
        } else {
            sb.append("feature/");
        }

        sb.append(issueKey);
        sb.append("-");

        Boolean lastReplaced=false;
        for (char c : issueSummary.toCharArray()) {
            /*
            if (c == ' ') {
                if (!lastReplaced) {
                    sb.append('-');
                }
                lastReplaced = true;
            } else
             */
            if (Character.isLetterOrDigit(c) || allowedSymbols.indexOf(c) >= 0) {
                sb.append(c);
                lastReplaced = false;
            } else {
                if (!lastReplaced) {
                    sb.append('-');
                }
                lastReplaced = true;
            }
        }
        return sb.toString();
    }
}
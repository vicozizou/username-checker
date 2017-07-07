package data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represents the result for the username check.
 * Contains the actual success flag, a message explaining what happened and, in case it applies, a list of username
 * suggestions.
 * @author victor.
 */
public class Result implements Serializable {
    private boolean success;
    private String messg;
    private List<String> suggestedUsernames;

    public Result(boolean success, List<String> suggestedUsernames, String messg) {
        this.success = success;
        this.messg = messg;
        this.suggestedUsernames = suggestedUsernames != null ? suggestedUsernames : Collections.emptyList();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessg() {
        return messg;
    }

    public void setMessg(String messg) {
        this.messg = messg;
    }

    public List<String> getSuggestedUsernames() {
        return suggestedUsernames;
    }

    public void setSuggestedUsernames(List<String> suggestedUsernames) {
        this.suggestedUsernames = suggestedUsernames;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("success=").append(success);
        if (messg != null) {
            sb.append(", messg='").append(messg).append('\'');
        }
        sb.append(", suggestedUsernames=").append(suggestedUsernames);
        sb.append('}');
        return sb.toString();
    }
}

package main.com.whitespell.peak.logic.endpoints.users;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.com.whitespell.peak.StaticRules;
import main.com.whitespell.peak.logic.Authentication;
import main.com.whitespell.peak.logic.EndpointHandler;
import main.com.whitespell.peak.logic.RequestObject;
import main.com.whitespell.peak.logic.config.Config;
import main.com.whitespell.peak.logic.endpoints.authentication.ExpireAuthentication;
import main.com.whitespell.peak.logic.logging.Logging;
import main.com.whitespell.peak.logic.sql.StatementExecutor;
import main.com.whitespell.peak.model.UserObject;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Cory McAn(cmcan), Whitespell LLC
 *         9/1/2015
 */

public class SendFeedback extends EndpointHandler {

    private static final String FEEDBACK_USER_ID_KEY = "userId";
    private static final String FEEDBACK_EMAIL_KEY = "email";
    private static final String FEEDBACK_MESSAGE_KEY = "message";

    private static final String INSERT_FEEDBACK = "INSERT INTO `feedback`(`feedback_message`,`email`) VALUES(?,?)";

    @Override
    protected void setUserInputs() {
        urlInput.put(FEEDBACK_USER_ID_KEY, StaticRules.InputTypes.REG_INT_REQUIRED);
        payloadInput.put(FEEDBACK_MESSAGE_KEY, StaticRules.InputTypes.REG_STRING_REQUIRED);
    }

    @Override
    public void safeCall(final RequestObject context) throws IOException {

        JsonObject json = context.getPayload().getAsJsonObject();

        int userId = Integer.parseInt(context.getUrlVariables().get(FEEDBACK_USER_ID_KEY));
        String message = json.get(FEEDBACK_MESSAGE_KEY).getAsString();

        /**
         * Get the user
         */

        /**
         * Currently the response for this object is only the values the user updated. This is to avoid an additional
         * get of the user's current fields.
         */

        HttpResponse<String> stringResponse = null;
        Gson g = new Gson();

        try {
            stringResponse = Unirest.get("http://localhost:" + Config.API_PORT + "/users/" + userId)
                    .header("accept", "application/json")
                    .header("X-Authentication", "-1," + StaticRules.MASTER_KEY + "")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        /**
         * Change only email
         */

        UserObject user = g.fromJson(stringResponse.getBody(), UserObject.class);

        if(user == null) {
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NULL_VALUE_FOUND);
            return;
        }

        /**
         * Ensure that the user is authenticated properly
         */

        final Authentication a = new Authentication(context.getRequest().getHeader("X-Authentication"));

        boolean isMe = (a.getUserId() == userId);

        if (!a.isAuthenticated()) {
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NOT_AUTHENTICATED);
            return;
        }


        /**
         * curl https://{subdomain}.zendesk.com/api/v2/tickets.json \
         -d '{"ticket": {"requester": {"name": "The Customer", "email": "thecustomer@domain.com"}, "subject": "My printer is on fire!", "comment": { "body": "The smoke is very colorful." }}}' \
         -H "Content-Type: application/json" -v -u {email_address}:{password} -X POST
         */

        try {
            stringResponse = Unirest.post("https://whitespell.zendesk.com/api/v2/tickets.json")
                    .header("Content-Type", "application/json")
                    .basicAuth("pim@whitespell.com", "XyK6bwhP")
                    .body("{\"ticket\": {\"requester\": {\"name\": \""+user.getDisplayName()+"\", \"email\": \""+user.getEmail()+"\"}, \"subject\": \"[PEAK]: "+user.getUserName()+"("+userId+"), "+(user.getPublisher() == 1 ? "PUB" : "USR") + " : " + message.substring(0,10) + "\", \"comment\": { \"body\": \""+message+"\" }}}")
                    .asString();

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        feedbackSuccessObject f = new feedbackSuccessObject();

        if(stringResponse.getStatus() == 201) {
            f.setSuccess(true);
        }

        String response = g.toJson(f);
        context.getResponse().setStatus(200);
        try {
            context.getResponse().getWriter().write(response);
        } catch (Exception e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }
    }

    public static class feedbackSuccessObject {

        boolean success;

        feedbackSuccessObject(){
            this.success = false;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

    }
}

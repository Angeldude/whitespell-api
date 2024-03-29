package main.com.whitespell.peak.logic.endpoints.users;

import com.google.gson.Gson;
import main.com.whitespell.peak.StaticRules;
import main.com.whitespell.peak.logic.EndpointHandler;
import main.com.whitespell.peak.logic.GenericAPIActions;
import main.com.whitespell.peak.logic.RequestObject;
import main.com.whitespell.peak.logic.logging.Logging;
import main.com.whitespell.peak.logic.sql.StatementExecutor;
import main.com.whitespell.peak.model.UserObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Pim de Witte(wwadewitte) & Cory McAn(cmcan), Whitespell LLC
 *         1/20/15
 *         whitespell.model
 */
public class GetUsers extends EndpointHandler {

	private static final String USER_ID = "user_id";

	private static final String USERNAME_KEY = "username";
    private static final String DISPLAYNAME_KEY = "displayname";
    private static final String EMAIL_KEY = "email";
	private static final String THUMBNAIL_KEY = "thumbnail";
	private static final String COVER_PHOTO_KEY = "cover_photo";
	private static final String SLOGAN_KEY = "slogan";
    private static final String PUBLISHER_KEY = "publisher";
    private static final String EXCLUDE_TEMP_USERS = "excludeTempUsers";

	@Override
	protected void setUserInputs() {
        queryStringInput.put(EXCLUDE_TEMP_USERS, StaticRules.InputTypes.REG_INT_OPTIONAL);
	}

	@Override
    public void safeCall(final RequestObject context) throws IOException {


        int offset=GenericAPIActions.getOffset(context.getQueryString()),
        limit=GenericAPIActions.getLimit(context.getQueryString());

        boolean excludeTempUsers = false;
        String[] excludeTempUsersValue = context.getQueryString().get(EXCLUDE_TEMP_USERS);
        if(excludeTempUsersValue != null) {
              if(excludeTempUsersValue[0].equals("1")) {
                  excludeTempUsers = true;
          }
        }

        String GET_USERS;
        String GET_USERS_EXCLUDE_TEMP;

        if(offset > 0){
            GET_USERS = "SELECT `user_id`, `username`, `displayname`, `email`, `thumbnail`, `cover_photo`, `slogan`, `publisher` FROM `user` WHERE `user_id` < ? ORDER BY `user_id` DESC LIMIT ?";
            GET_USERS_EXCLUDE_TEMP = "SELECT `user_id`, `username`, `displayname`, `email`, `thumbnail`, `cover_photo`, `slogan`, `publisher` FROM `user` WHERE `user_id` < ?  AND `email` NOT LIKE '%temporary.email' ORDER BY `user_id` DESC LIMIT ?";
        }else{
            GET_USERS = "SELECT `user_id`, `username`, `displayname`, `email`, `thumbnail`, `cover_photo`, `slogan`, `publisher` FROM `user` WHERE `user_id` > ? ORDER BY `user_id` DESC LIMIT ?";
            GET_USERS_EXCLUDE_TEMP = "SELECT `user_id`, `username`, `displayname`, `email`, `thumbnail`, `cover_photo`, `slogan`, `publisher` FROM `user` WHERE `user_id` > ?  AND `email` NOT LIKE '%temporary.email' ORDER BY `user_id` DESC LIMIT ?";
        }

        try {
            StatementExecutor executor = new StatementExecutor(excludeTempUsers ? GET_USERS_EXCLUDE_TEMP : GET_USERS);

            executor.execute(ps -> {

                ps.setInt(1, offset);
                ps.setInt(2, limit);
                final ResultSet results = ps.executeQuery();
                ArrayList<UserObject> users = new ArrayList<>();

                while (results.next()) {

                    UserObject d = new UserObject(results.getInt(USER_ID), results.getString(USERNAME_KEY), results.getString(DISPLAYNAME_KEY),
                            results.getString(EMAIL_KEY), results.getString(THUMBNAIL_KEY), results.getString(COVER_PHOTO_KEY),
                            results.getString(SLOGAN_KEY), results.getInt(PUBLISHER_KEY));

                    users.add(d);
                }

                // put the array list into a JSON array and write it as a response

                Gson g = new Gson();
                String response = g.toJson(users);
                context.getResponse().setStatus(200);
                try {
                    context.getResponse().getWriter().write(response);
                } catch (Exception e) {
                    Logging.log("High", e);
                    context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
                    return;
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }
    }
}

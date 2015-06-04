package whitespell.peakapi.endpoints.users;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import whitespell.StaticRules;
import whitespell.logic.ApiInterface;
import whitespell.logic.RequestContext;
import whitespell.logic.SessionIdentifierGenerator;
import whitespell.logic.logging.Logging;
import whitespell.logic.sql.ExecutionBlock;
import whitespell.logic.sql.Pool;
import whitespell.logic.sql.StatementExecutor;
import whitespell.model.AuthenticationObject;
import whitespell.security.PasswordHash;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Pim de Witte(wwadewitte), Whitespell LLC
 *         1/20/15
 *         whitespell.model
 */
public class AuthenticationRequest implements ApiInterface {


    private static final String RETRIEVE_PASSWORD = "SELECT `user_id`,`password` FROM `users` WHERE `username` = ? LIMIT 1";

    private static final String INSERT_AUTHENTICATION = "INSERT INTO `authentication`(`user_id`, `key`) " +
            "VALUES (?,?)";

    @Override
    public void call(final RequestContext context) throws IOException {

        Connection con;
        final String username;
        final String password;

        JsonObject payload = context.getPayload().getAsJsonObject();

        /**
         * 400 Bad Request: Check if all data is valid
         */

        // Check if all parameters are present and contain the right characters, if not throw a 400
        if (payload == null || payload.get("username") == null || payload.get("password") == null) {
            context.throwHttpError(StaticRules.ErrorCodes.NULL_VALUE_FOUND);
            return;
        } else {
            username = payload.get("username").getAsString();
            password = payload.get("password").getAsString();
            // check against lengths for security and UX reasons.

            //check if values are too long
            if (username.length() > StaticRules.MAX_USERNAME_LENGTH) {
                context.throwHttpError(StaticRules.ErrorCodes.USERNAME_TOO_LONG);
                return;
            } else if (password.length() > StaticRules.MAX_PASSWORD_LENGTH) {
                context.throwHttpError(StaticRules.ErrorCodes.PASSWORD_TOO_LONG);
                return;
            } else if (username.length() < StaticRules.MIN_USERNAME_LENGTH) {
                context.throwHttpError(StaticRules.ErrorCodes.USERNAME_TOO_SHORT);
                return;
            } else if (password.length() < StaticRules.MIN_PASSWORD_LENGTH) {
                context.throwHttpError(StaticRules.ErrorCodes.PASSWORD_TOO_SHORT);
                return;
            }
        }


        // retrieve the password based on the username
        try {
            StatementExecutor executor = new StatementExecutor(RETRIEVE_PASSWORD);

            executor.execute(new ExecutionBlock() {
                @Override
                public void process(PreparedStatement ps) throws SQLException {
                    ps.setString(1, username);
                    final ResultSet s = ps.executeQuery();
                    if (s.next()) {
                        try {
                            // with the result set, check if password is verified
                            boolean isVerified = PasswordHash.validatePassword(password, s.getString("password"));

                            if (isVerified) {
                                // initialize an authenticationobject and set the authentication key if verified
                                final AuthenticationObject ao = new AuthenticationObject();
                                ao.setKey(SessionIdentifierGenerator.nextSessionId());
                                ao.setUserId(s.getInt("user_id"));
                                // insert the new authentication key into the database
                                try {
                                    StatementExecutor executor = new StatementExecutor(INSERT_AUTHENTICATION);

                                    executor.execute(new ExecutionBlock() {
                                        @Override
                                        public void process(PreparedStatement ps) throws SQLException {
                                            ps.setInt(1, ao.getUserId());
                                            ps.setString(2, ao.getKey());
                                            ps.executeUpdate();

                                        }
                                    });
                                } catch (SQLException e) {
                                    Logging.log("High", e);
                                }
                                // build a gson object based on the authentication object
                                Gson g = new Gson();
                                String jsonAo = g.toJson(ao);
                                // write the authentication object and the session key and return.
                                context.getResponse().getWriter().write(jsonAo);
                                return;
                            } else {
                                // if not verified, throw error
                                context.throwHttpError(StaticRules.ErrorCodes.INVALID_USERNAME_OR_PASS);
                                return;
                            }
                        } catch (NoSuchAlgorithmException e) {
                            Logging.log("High", e);
                        } catch (InvalidKeySpecException e) {
                            Logging.log("High", e);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        context.throwHttpError(StaticRules.ErrorCodes.ACCOUNT_NOT_FOUND);
                        return;
                    }
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
        }
    }

}

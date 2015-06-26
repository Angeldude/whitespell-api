package main.com.whitespell.peak.logic.endpoints;

import main.com.whitespell.peak.logic.EndpointDispatcher;
import main.com.whitespell.peak.logic.baseapi.WhitespellAPI;
import main.com.whitespell.peak.logic.endpoints.authentication.AuthenticationRequest;
import main.com.whitespell.peak.logic.endpoints.content.AddNewContent;
import main.com.whitespell.peak.logic.endpoints.content.RequestContent;
import main.com.whitespell.peak.logic.endpoints.content.categories.AddCategory;
import main.com.whitespell.peak.logic.endpoints.content.categories.RequestCategories;
import main.com.whitespell.peak.logic.endpoints.content.types.AddContentType;
import main.com.whitespell.peak.logic.endpoints.content.types.RequestContentTypes;
import main.com.whitespell.peak.logic.endpoints.ping.Ping;
import main.com.whitespell.peak.logic.endpoints.statistics.GetUserSignups;
import main.com.whitespell.peak.logic.endpoints.users.*;
import main.com.whitespell.peak.logic.endpoints.users.publishers.GetUsersByCategory;

/**
 * @author Pim de Witte(wwadewitte), Whitespell LLC
 *         2/4/15
 *         whitespell.endpoints
 */
public class PeakAPI extends WhitespellAPI {

    /**
     * MyEndpoints schedules the endpoints in the API.
     *
     * @param dispatcher
     */

    @Override
    protected void scheduleEndpoints(EndpointDispatcher dispatcher) {

        /**
         * PING
         */

        // Simple ping reques that returns true.
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new Ping(), "/ping");

        /**
         * USERS ENDPOINTS (TREE ORDER)
         */

        // get the list of users based on search criteria
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new GetUsers(), "/users");

        // add a new user (create account)
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new CreateUser(), "/users");

        // Get a specific user based on their user ID
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new GetUser(), "/users/$", "user_id"); //always have the variable first

        // get all the users sorted by categories (also takes in same search criteria as /users)
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new GetUsersByCategory(), "/users/categories/");

        // Follow a user
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new UserFollowAction(), "/users/$/following", "user_id");

        // Follow a category
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new CategoryFollowAction(), "/users/$/categories", "user_id");

        // Publish in a category
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new CategoryPublishAction(), "/users/$/publishing", "user_id");

        /**
         * STATISTICS
         */

        // Get some sign up data
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new GetUserSignups(), "/statistics/signups");

        /**
         * AUTHENTICATION
         */

        //todo move auth under users
        //Authenticate a user
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new AuthenticationRequest(), "/authentication");


        /**
         * CONTENT
         */

        // Get a list of all the content in the whole system based on certain search criteria
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new RequestContent(), "/content/");

        // Add new content as a user
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new AddNewContent(), "/content/$", "user_id");

        // Get a list of all the content types
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new RequestContentTypes(), "/content/types");

        // Add a new content type
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new AddContentType(), "/content/types");

        /**
         * CATEGORIES
         */

        // Get a list of all the categories
        dispatcher.addHandler(EndpointDispatcher.RequestType.GET, new RequestCategories(), "/categories");

        // Add a new category
        dispatcher.addHandler(EndpointDispatcher.RequestType.POST, new AddCategory(), "/categories");



    }

}

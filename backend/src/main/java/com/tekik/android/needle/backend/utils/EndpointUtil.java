package com.tekik.android.needle.backend.utils;

import com.tekik.android.needle.backend.models.UserAccount;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import static com.tekik.android.needle.backend.OfyService.ofy;

/**
 * Created by jonfisk on 04/09/15.
 */
public class EndpointUtil {

    /**
     * Throws an exception if the user is not an admin.
     * @param user User object to be checked if it represents an admin.
     * @throws com.google.api.server.spi.response.UnauthorizedException when the
     *      user object does not represent an admin.
     */
    public static void throwIfNotAdmin(final User user) throws
            UnauthorizedException {
        if (!UserAccount.isAdmin(user)) {
            throw new UnauthorizedException(
                    "You are not authorized to perform this operation");
        }
    }

    /**
     * Throws an exception if the user object doesn't represent an authenticated
     * call.
     * @param user User object to be checked if it represents an authenticated
     *      caller.
     * @throws com.google.api.server.spi.response.UnauthorizedException when the
     *      user object does not represent an admin.
     */
    public static void throwIfNotAuthenticated(final User user) throws
            UnauthorizedException {
        if (user == null || user.getEmail() == null) {
            throw new UnauthorizedException(
                    "Only authenticated users may invoke this operation");
        }
    }

    @SuppressWarnings({"cast", "unchecked"})
    public static final UserAccount getUserAccountbyEmail(final String email) {
        return ofy().load().type(UserAccount.class)
                .filter("email", email).first().now();
    }
}

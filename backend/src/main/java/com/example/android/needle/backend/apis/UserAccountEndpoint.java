package com.example.android.needle.backend.apis;

/**
 * Created by jonfisk on 08/09/15.
 */

import com.example.android.needle.backend.Constants;
import com.example.android.needle.backend.models.UserAccount;
import com.example.android.needle.backend.utils.EndpointUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import static com.example.android.needle.backend.OfyService.ofy;

/**
 * Exposes REST API over Place resources.
 */
@Api(name = "needle", version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        )
)
@ApiClass(resource = "accounts",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class UserAccountEndpoint {

    private static final Logger log = Logger.getLogger(UserAccountEndpoint.class.getName());


    @ApiMethod(httpMethod = "POST")
    public final UserAccount insertUserAccount(final UserAccount userAccount) {



        UserAccount account = EndpointUtil.getUserAccountbyEmail(userAccount.getEmail());

        if(account == null)
            ofy().save().entity(userAccount).now();

        return userAccount;
    }
}

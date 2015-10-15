package com.tekik.android.needle.backend.apis;

/**
 * Created by jonfisk on 08/09/15.
 */

import com.tekik.android.needle.backend.Constants;
import com.tekik.android.needle.backend.models.UserAccount;
import com.tekik.android.needle.backend.utils.EndpointUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.tekik.android.needle.backend.OfyService;

import java.util.logging.Logger;

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
            OfyService.ofy().save().entity(userAccount).now();

        return userAccount;
    }
}

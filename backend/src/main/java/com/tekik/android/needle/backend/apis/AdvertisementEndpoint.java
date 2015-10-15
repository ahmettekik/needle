package com.tekik.android.needle.backend.apis;

/**
 * Created by jonfisk on 07/09/15.
 */


import com.tekik.android.needle.backend.Constants;
import com.tekik.android.needle.backend.models.Advertisement;
import com.tekik.android.needle.backend.utils.EndpointUtil;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;

import java.util.Date;
import java.util.List;

import static com.tekik.android.needle.backend.OfyService.ofy;

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
@ApiClass(resource = "ads",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)


public class AdvertisementEndpoint {

    /**
     * Log output.
     */


    private static final long HOURINMILLIS = 60 * 60 * 1000;



    @ApiMethod(httpMethod = "GET")
    public final List<Advertisement> getNeighborhoodAds(
            @Named("countryCode") final String countryCode,
            @Named("zipCode") final String zipCode,
            @Named("date") final Date date,
            final User user) throws ServiceException {

        EndpointUtil.throwIfNotAdmin(user);


        return ofy()
                .load()
                .type(Advertisement.class)
                .filter("countryCode", countryCode)
                .filter("zipCode", zipCode)
                .filter("date >", date)
                .order("-date")
                .list();
    }


    @ApiMethod(httpMethod = "GET")
    public final Advertisement getAdvertisement(@Named("id") final Long id, final User user)
            throws ServiceException{
        EndpointUtil.throwIfNotAdmin(user);

        return findAdvertisement(id);
    }

    @ApiMethod(httpMethod = "POST")
    public final Advertisement insertAdvertisement(final Advertisement advertisement,
                                                   final User user) throws
            ServiceException {
        EndpointUtil.throwIfNotAdmin(user);

        ofy().save().entity(advertisement).now();

        return advertisement;
    }



    @ApiMethod(httpMethod = "PUT")
    public final Advertisement updateAd(final Advertisement advertisement,
                                        final User user) throws
            ServiceException {
        EndpointUtil.throwIfNotAdmin(user);

        ofy().save().entity(advertisement).now();

        return advertisement;
    }

    @ApiMethod(httpMethod = "DELETE")
    public final void removeAdvertisement(@Named("id") final Long id, final User user) throws
            ServiceException {
        EndpointUtil.throwIfNotAdmin(user);

        Advertisement advertisement = findAdvertisement(id);
        if(advertisement == null) {
            return;
        }
        ofy().delete().entity(advertisement).now();

    }



    private Advertisement findAdvertisement(Long id) {
        return ofy().load().type(Advertisement.class).id(id).now();
    }
}

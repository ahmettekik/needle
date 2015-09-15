/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-08-03 17:34:38 UTC)
 * on 2015-09-14 at 18:46:31 UTC 
 * Modify at your own risk.
 */

package com.example.android.needle.backend.needle;

/**
 * Service definition for Needle (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link NeedleRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Needle extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.20.0 of the needle library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://needle-a852b.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "needle/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Needle(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Needle(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the Accounts collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Needle needle = new Needle(...);}
   *   {@code Needle.Accounts.List request = needle.accounts().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Accounts accounts() {
    return new Accounts();
  }

  /**
   * The "accounts" collection of methods.
   */
  public class Accounts {

    /**
     * Create a request for the method "accounts.insertUserAccount".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link InsertUserAccount#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.example.android.needle.backend.needle.model.UserAccount}
     * @return the request
     */
    public InsertUserAccount insertUserAccount(com.example.android.needle.backend.needle.model.UserAccount content) throws java.io.IOException {
      InsertUserAccount result = new InsertUserAccount(content);
      initialize(result);
      return result;
    }

    public class InsertUserAccount extends NeedleRequest<com.example.android.needle.backend.needle.model.UserAccount> {

      private static final String REST_PATH = "accounts";

      /**
       * Create a request for the method "accounts.insertUserAccount".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link InsertUserAccount#execute()} method to invoke the remote operation.
       * <p> {@link InsertUserAccount#initialize(com.google.api.client.googleapis.services.AbstractGoogl
       * eClientRequest)} must be called to initialize this instance immediately after invoking the
       * constructor. </p>
       *
       * @param content the {@link com.example.android.needle.backend.needle.model.UserAccount}
       * @since 1.13
       */
      protected InsertUserAccount(com.example.android.needle.backend.needle.model.UserAccount content) {
        super(Needle.this, "POST", REST_PATH, content, com.example.android.needle.backend.needle.model.UserAccount.class);
      }

      @Override
      public InsertUserAccount setAlt(java.lang.String alt) {
        return (InsertUserAccount) super.setAlt(alt);
      }

      @Override
      public InsertUserAccount setFields(java.lang.String fields) {
        return (InsertUserAccount) super.setFields(fields);
      }

      @Override
      public InsertUserAccount setKey(java.lang.String key) {
        return (InsertUserAccount) super.setKey(key);
      }

      @Override
      public InsertUserAccount setOauthToken(java.lang.String oauthToken) {
        return (InsertUserAccount) super.setOauthToken(oauthToken);
      }

      @Override
      public InsertUserAccount setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (InsertUserAccount) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public InsertUserAccount setQuotaUser(java.lang.String quotaUser) {
        return (InsertUserAccount) super.setQuotaUser(quotaUser);
      }

      @Override
      public InsertUserAccount setUserIp(java.lang.String userIp) {
        return (InsertUserAccount) super.setUserIp(userIp);
      }

      @Override
      public InsertUserAccount set(String parameterName, Object value) {
        return (InsertUserAccount) super.set(parameterName, value);
      }
    }

  }

  /**
   * An accessor for creating requests from the Ads collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Needle needle = new Needle(...);}
   *   {@code Needle.Ads.List request = needle.ads().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Ads ads() {
    return new Ads();
  }

  /**
   * The "ads" collection of methods.
   */
  public class Ads {

    /**
     * Create a request for the method "ads.getAdvertisement".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link GetAdvertisement#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public GetAdvertisement getAdvertisement(java.lang.Long id) throws java.io.IOException {
      GetAdvertisement result = new GetAdvertisement(id);
      initialize(result);
      return result;
    }

    public class GetAdvertisement extends NeedleRequest<com.example.android.needle.backend.needle.model.Advertisement> {

      private static final String REST_PATH = "ads/{id}";

      /**
       * Create a request for the method "ads.getAdvertisement".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link GetAdvertisement#execute()} method to invoke the remote operation.
       * <p> {@link GetAdvertisement#initialize(com.google.api.client.googleapis.services.AbstractGoogle
       * ClientRequest)} must be called to initialize this instance immediately after invoking the
       * constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected GetAdvertisement(java.lang.Long id) {
        super(Needle.this, "GET", REST_PATH, null, com.example.android.needle.backend.needle.model.Advertisement.class);
        this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetAdvertisement setAlt(java.lang.String alt) {
        return (GetAdvertisement) super.setAlt(alt);
      }

      @Override
      public GetAdvertisement setFields(java.lang.String fields) {
        return (GetAdvertisement) super.setFields(fields);
      }

      @Override
      public GetAdvertisement setKey(java.lang.String key) {
        return (GetAdvertisement) super.setKey(key);
      }

      @Override
      public GetAdvertisement setOauthToken(java.lang.String oauthToken) {
        return (GetAdvertisement) super.setOauthToken(oauthToken);
      }

      @Override
      public GetAdvertisement setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetAdvertisement) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetAdvertisement setQuotaUser(java.lang.String quotaUser) {
        return (GetAdvertisement) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetAdvertisement setUserIp(java.lang.String userIp) {
        return (GetAdvertisement) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.Long id;

      /**

       */
      public java.lang.Long getId() {
        return id;
      }

      public GetAdvertisement setId(java.lang.Long id) {
        this.id = id;
        return this;
      }

      @Override
      public GetAdvertisement set(String parameterName, Object value) {
        return (GetAdvertisement) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "ads.getNeighborhoodAds".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link GetNeighborhoodAds#execute()} method to invoke the remote operation.
     *
     * @param countryCode
     * @param zipCode
     * @return the request
     */
    public GetNeighborhoodAds getNeighborhoodAds(java.lang.String countryCode, java.lang.String zipCode) throws java.io.IOException {
      GetNeighborhoodAds result = new GetNeighborhoodAds(countryCode, zipCode);
      initialize(result);
      return result;
    }

    public class GetNeighborhoodAds extends NeedleRequest<com.example.android.needle.backend.needle.model.AdvertisementCollection> {

      private static final String REST_PATH = "ads/{countryCode}/{zipCode}";

      /**
       * Create a request for the method "ads.getNeighborhoodAds".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link GetNeighborhoodAds#execute()} method to invoke the remote
       * operation. <p> {@link GetNeighborhoodAds#initialize(com.google.api.client.googleapis.services.A
       * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @param countryCode
       * @param zipCode
       * @since 1.13
       */
      protected GetNeighborhoodAds(java.lang.String countryCode, java.lang.String zipCode) {
        super(Needle.this, "GET", REST_PATH, null, com.example.android.needle.backend.needle.model.AdvertisementCollection.class);
        this.countryCode = com.google.api.client.util.Preconditions.checkNotNull(countryCode, "Required parameter countryCode must be specified.");
        this.zipCode = com.google.api.client.util.Preconditions.checkNotNull(zipCode, "Required parameter zipCode must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetNeighborhoodAds setAlt(java.lang.String alt) {
        return (GetNeighborhoodAds) super.setAlt(alt);
      }

      @Override
      public GetNeighborhoodAds setFields(java.lang.String fields) {
        return (GetNeighborhoodAds) super.setFields(fields);
      }

      @Override
      public GetNeighborhoodAds setKey(java.lang.String key) {
        return (GetNeighborhoodAds) super.setKey(key);
      }

      @Override
      public GetNeighborhoodAds setOauthToken(java.lang.String oauthToken) {
        return (GetNeighborhoodAds) super.setOauthToken(oauthToken);
      }

      @Override
      public GetNeighborhoodAds setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetNeighborhoodAds) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetNeighborhoodAds setQuotaUser(java.lang.String quotaUser) {
        return (GetNeighborhoodAds) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetNeighborhoodAds setUserIp(java.lang.String userIp) {
        return (GetNeighborhoodAds) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String countryCode;

      /**

       */
      public java.lang.String getCountryCode() {
        return countryCode;
      }

      public GetNeighborhoodAds setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
        return this;
      }

      @com.google.api.client.util.Key
      private java.lang.String zipCode;

      /**

       */
      public java.lang.String getZipCode() {
        return zipCode;
      }

      public GetNeighborhoodAds setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
        return this;
      }

      @Override
      public GetNeighborhoodAds set(String parameterName, Object value) {
        return (GetNeighborhoodAds) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "ads.insertAdvertisement".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link InsertAdvertisement#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.example.android.needle.backend.needle.model.Advertisement}
     * @return the request
     */
    public InsertAdvertisement insertAdvertisement(com.example.android.needle.backend.needle.model.Advertisement content) throws java.io.IOException {
      InsertAdvertisement result = new InsertAdvertisement(content);
      initialize(result);
      return result;
    }

    public class InsertAdvertisement extends NeedleRequest<com.example.android.needle.backend.needle.model.Advertisement> {

      private static final String REST_PATH = "ads";

      /**
       * Create a request for the method "ads.insertAdvertisement".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link InsertAdvertisement#execute()} method to invoke the remote
       * operation. <p> {@link InsertAdvertisement#initialize(com.google.api.client.googleapis.services.
       * AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @param content the {@link com.example.android.needle.backend.needle.model.Advertisement}
       * @since 1.13
       */
      protected InsertAdvertisement(com.example.android.needle.backend.needle.model.Advertisement content) {
        super(Needle.this, "POST", REST_PATH, content, com.example.android.needle.backend.needle.model.Advertisement.class);
      }

      @Override
      public InsertAdvertisement setAlt(java.lang.String alt) {
        return (InsertAdvertisement) super.setAlt(alt);
      }

      @Override
      public InsertAdvertisement setFields(java.lang.String fields) {
        return (InsertAdvertisement) super.setFields(fields);
      }

      @Override
      public InsertAdvertisement setKey(java.lang.String key) {
        return (InsertAdvertisement) super.setKey(key);
      }

      @Override
      public InsertAdvertisement setOauthToken(java.lang.String oauthToken) {
        return (InsertAdvertisement) super.setOauthToken(oauthToken);
      }

      @Override
      public InsertAdvertisement setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (InsertAdvertisement) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public InsertAdvertisement setQuotaUser(java.lang.String quotaUser) {
        return (InsertAdvertisement) super.setQuotaUser(quotaUser);
      }

      @Override
      public InsertAdvertisement setUserIp(java.lang.String userIp) {
        return (InsertAdvertisement) super.setUserIp(userIp);
      }

      @Override
      public InsertAdvertisement set(String parameterName, Object value) {
        return (InsertAdvertisement) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "ads.removeAdvertisement".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link RemoveAdvertisement#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public RemoveAdvertisement removeAdvertisement(java.lang.Long id) throws java.io.IOException {
      RemoveAdvertisement result = new RemoveAdvertisement(id);
      initialize(result);
      return result;
    }

    public class RemoveAdvertisement extends NeedleRequest<Void> {

      private static final String REST_PATH = "ads/{id}";

      /**
       * Create a request for the method "ads.removeAdvertisement".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link RemoveAdvertisement#execute()} method to invoke the remote
       * operation. <p> {@link RemoveAdvertisement#initialize(com.google.api.client.googleapis.services.
       * AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected RemoveAdvertisement(java.lang.Long id) {
        super(Needle.this, "DELETE", REST_PATH, null, Void.class);
        this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public RemoveAdvertisement setAlt(java.lang.String alt) {
        return (RemoveAdvertisement) super.setAlt(alt);
      }

      @Override
      public RemoveAdvertisement setFields(java.lang.String fields) {
        return (RemoveAdvertisement) super.setFields(fields);
      }

      @Override
      public RemoveAdvertisement setKey(java.lang.String key) {
        return (RemoveAdvertisement) super.setKey(key);
      }

      @Override
      public RemoveAdvertisement setOauthToken(java.lang.String oauthToken) {
        return (RemoveAdvertisement) super.setOauthToken(oauthToken);
      }

      @Override
      public RemoveAdvertisement setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (RemoveAdvertisement) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public RemoveAdvertisement setQuotaUser(java.lang.String quotaUser) {
        return (RemoveAdvertisement) super.setQuotaUser(quotaUser);
      }

      @Override
      public RemoveAdvertisement setUserIp(java.lang.String userIp) {
        return (RemoveAdvertisement) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.Long id;

      /**

       */
      public java.lang.Long getId() {
        return id;
      }

      public RemoveAdvertisement setId(java.lang.Long id) {
        this.id = id;
        return this;
      }

      @Override
      public RemoveAdvertisement set(String parameterName, Object value) {
        return (RemoveAdvertisement) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "ads.removeAfterAnHour".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link RemoveAfterAnHour#execute()} method to invoke the remote operation.
     *
     * @param countryCode
     * @param zipCode
     * @return the request
     */
    public RemoveAfterAnHour removeAfterAnHour(java.lang.String countryCode, java.lang.String zipCode) throws java.io.IOException {
      RemoveAfterAnHour result = new RemoveAfterAnHour(countryCode, zipCode);
      initialize(result);
      return result;
    }

    public class RemoveAfterAnHour extends NeedleRequest<Void> {

      private static final String REST_PATH = "ads/{countryCode}/{zipCode}";

      /**
       * Create a request for the method "ads.removeAfterAnHour".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link RemoveAfterAnHour#execute()} method to invoke the remote operation.
       * <p> {@link RemoveAfterAnHour#initialize(com.google.api.client.googleapis.services.AbstractGoogl
       * eClientRequest)} must be called to initialize this instance immediately after invoking the
       * constructor. </p>
       *
       * @param countryCode
       * @param zipCode
       * @since 1.13
       */
      protected RemoveAfterAnHour(java.lang.String countryCode, java.lang.String zipCode) {
        super(Needle.this, "DELETE", REST_PATH, null, Void.class);
        this.countryCode = com.google.api.client.util.Preconditions.checkNotNull(countryCode, "Required parameter countryCode must be specified.");
        this.zipCode = com.google.api.client.util.Preconditions.checkNotNull(zipCode, "Required parameter zipCode must be specified.");
      }

      @Override
      public RemoveAfterAnHour setAlt(java.lang.String alt) {
        return (RemoveAfterAnHour) super.setAlt(alt);
      }

      @Override
      public RemoveAfterAnHour setFields(java.lang.String fields) {
        return (RemoveAfterAnHour) super.setFields(fields);
      }

      @Override
      public RemoveAfterAnHour setKey(java.lang.String key) {
        return (RemoveAfterAnHour) super.setKey(key);
      }

      @Override
      public RemoveAfterAnHour setOauthToken(java.lang.String oauthToken) {
        return (RemoveAfterAnHour) super.setOauthToken(oauthToken);
      }

      @Override
      public RemoveAfterAnHour setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (RemoveAfterAnHour) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public RemoveAfterAnHour setQuotaUser(java.lang.String quotaUser) {
        return (RemoveAfterAnHour) super.setQuotaUser(quotaUser);
      }

      @Override
      public RemoveAfterAnHour setUserIp(java.lang.String userIp) {
        return (RemoveAfterAnHour) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String countryCode;

      /**

       */
      public java.lang.String getCountryCode() {
        return countryCode;
      }

      public RemoveAfterAnHour setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
        return this;
      }

      @com.google.api.client.util.Key
      private java.lang.String zipCode;

      /**

       */
      public java.lang.String getZipCode() {
        return zipCode;
      }

      public RemoveAfterAnHour setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
        return this;
      }

      @Override
      public RemoveAfterAnHour set(String parameterName, Object value) {
        return (RemoveAfterAnHour) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "ads.updateAd".
     *
     * This request holds the parameters needed by the needle server.  After setting any optional
     * parameters, call the {@link UpdateAd#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.example.android.needle.backend.needle.model.Advertisement}
     * @return the request
     */
    public UpdateAd updateAd(com.example.android.needle.backend.needle.model.Advertisement content) throws java.io.IOException {
      UpdateAd result = new UpdateAd(content);
      initialize(result);
      return result;
    }

    public class UpdateAd extends NeedleRequest<com.example.android.needle.backend.needle.model.Advertisement> {

      private static final String REST_PATH = "ads";

      /**
       * Create a request for the method "ads.updateAd".
       *
       * This request holds the parameters needed by the the needle server.  After setting any optional
       * parameters, call the {@link UpdateAd#execute()} method to invoke the remote operation. <p>
       * {@link
       * UpdateAd#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
       * must be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param content the {@link com.example.android.needle.backend.needle.model.Advertisement}
       * @since 1.13
       */
      protected UpdateAd(com.example.android.needle.backend.needle.model.Advertisement content) {
        super(Needle.this, "PUT", REST_PATH, content, com.example.android.needle.backend.needle.model.Advertisement.class);
      }

      @Override
      public UpdateAd setAlt(java.lang.String alt) {
        return (UpdateAd) super.setAlt(alt);
      }

      @Override
      public UpdateAd setFields(java.lang.String fields) {
        return (UpdateAd) super.setFields(fields);
      }

      @Override
      public UpdateAd setKey(java.lang.String key) {
        return (UpdateAd) super.setKey(key);
      }

      @Override
      public UpdateAd setOauthToken(java.lang.String oauthToken) {
        return (UpdateAd) super.setOauthToken(oauthToken);
      }

      @Override
      public UpdateAd setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (UpdateAd) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public UpdateAd setQuotaUser(java.lang.String quotaUser) {
        return (UpdateAd) super.setQuotaUser(quotaUser);
      }

      @Override
      public UpdateAd setUserIp(java.lang.String userIp) {
        return (UpdateAd) super.setUserIp(userIp);
      }

      @Override
      public UpdateAd set(String parameterName, Object value) {
        return (UpdateAd) super.set(parameterName, value);
      }
    }

  }

  /**
   * Builder for {@link Needle}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Needle}. */
    @Override
    public Needle build() {
      return new Needle(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link NeedleRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setNeedleRequestInitializer(
        NeedleRequestInitializer needleRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(needleRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}

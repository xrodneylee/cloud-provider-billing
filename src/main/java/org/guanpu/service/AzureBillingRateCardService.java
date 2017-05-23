package org.guanpu.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;

public class AzureBillingRateCardService {
	
	private final String ROOT_URL = "https://management.azure.com";
	private final String SUBSCRIPTION_PATH = "subscriptions";
	private final String RATECARD_RESOURCE_PATH = "providers/Microsoft.Commerce/RateCard";
	private final String API_VERSION_KET = "api-version";
	private final String RATECARD_API_VERSION = "2016-08-31-preview";
	
	private final String subscription;
	private final String offerDurableId;
	private final String currency;
	private final String locale;
	private final String regionInfo;
	private final String accessToken;
	
	private AzureBillingRateCardService(Builder builder){
		this.subscription = builder.subscription;
		this.offerDurableId = builder.offerDurableId;
		this.currency = builder.currency;
		this.locale = builder.locale;
		this.regionInfo = builder.regionInfo;
		this.accessToken = builder.accessToken;
	}
	
	
	public Response invoke(){
		Client client = ClientBuilder.newClient();
		String authorization = "Bearer " + accessToken;
		
		StringBuilder sb = new StringBuilder()
			.append("OfferDurableId eq '").append(offerDurableId)
			.append("' and Currency eq '").append(currency)
			.append("' and Locale eq '").append(locale)
			.append("' and RegionInfo eq '").append(regionInfo).append("'");
		
		WebTarget webTarget = client.target(ROOT_URL).path(SUBSCRIPTION_PATH).path(subscription).path(RATECARD_RESOURCE_PATH)
					.queryParam(API_VERSION_KET, RATECARD_API_VERSION)
					.queryParam("$filter", sb.toString());
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON)
														 .header(HttpHeaders.AUTHORIZATION, authorization);
		
		Response response = invocationBuilder.get();
		
		return response;
	}
	
	public static final class Builder {

		private String subscription;
		private String offerDurableId;
		private String currency;
		private String locale;
		private String regionInfo;
		private String accessToken;
		
		public AzureBillingRateCardService build(){
			Preconditions.checkNotNull(subscription, "subscription can't be null");
			Preconditions.checkNotNull(offerDurableId, "offerDurableId can't be null");
			Preconditions.checkNotNull(currency, "currency can't be null");
			Preconditions.checkNotNull(locale, "locale can't be null");
			Preconditions.checkNotNull(regionInfo, "regionInfo can't be null");
			Preconditions.checkNotNull(accessToken, "accessToken can't be null");
			return new AzureBillingRateCardService(this);
		}
		
		public Builder setAccessToken(String accessToken) {
			this.accessToken = accessToken;
			return this;
		}
		
		public Builder setSubscription(String subscription) {
			this.subscription = subscription;
			return this;
		}
		
		public Builder setOfferDurableId(String offerDurableId) {
			this.offerDurableId = offerDurableId;
			return this;
		}
		
		public Builder setCurrency(String currency) {
			this.currency = currency;
			return this;
		}
		
		public Builder setLocale(String locale) {
			this.locale = locale;
			return this;
		}
		
		public Builder setRegionInfo(String regionInfo) {
			this.regionInfo = regionInfo;
			return this;
		}

	}
	
}

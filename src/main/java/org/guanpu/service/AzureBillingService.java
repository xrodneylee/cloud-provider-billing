package org.guanpu.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AzureBillingService {
	
	private final String ROOT_URL = "https://management.azure.com";
	private final String SUBSCRIPTION_PATH = "subscriptions";
	private final String RATECARD_RESOURCE_PATH = "providers/Microsoft.Commerce/RateCard";
	private final String API_VERSION_KET = "api-version";
	private final String RATECARD_API_VERSION_VALUE = "2016-08-31-preview";
//	private final String USAGE_API_VERSION = "2015-06-01-preview";
	
	private final String subscription;
	private final String offerDurableId;
	private final String currency;
	private final String locale;
	private final String regionInfo;
	
	private AzureBillingService(Builder builder){
		this.subscription = builder.subscription;
		this.offerDurableId = builder.offerDurableId;
		this.currency = builder.currency;
		this.locale = builder.locale;
		this.regionInfo = builder.regionInfo;
	}
	
	public Response invokeRateCard(String tokenType, String accessToken){
		Client client = ClientBuilder.newClient();
		String authorization = tokenType + " " + accessToken;
		
		StringBuilder sb = new StringBuilder()
			.append("OfferDurableId eq '").append(offerDurableId)
			.append("' and Currency eq '").append(currency)
			.append("' and Locale eq '").append(locale)
			.append("' and RegionInfo eq '").append(regionInfo).append("'");
		
		WebTarget webTarget = client.target(ROOT_URL).path(SUBSCRIPTION_PATH).path(subscription).path(RATECARD_RESOURCE_PATH)
					.queryParam(API_VERSION_KET, RATECARD_API_VERSION_VALUE)
					.queryParam("$filter", sb.toString());
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON)
														 .header(HttpHeaders.AUTHORIZATION, authorization);
		
		Response response = invocationBuilder.get();
		
		return response;
	}
	
//	public String invokeUsage(){
//		return "";
//	}
	
	public static final class Builder {

		private String subscription;
		private String offerDurableId;
		private String currency;
		private String locale;
		private String regionInfo;
		
		public AzureBillingService build(){
			return new AzureBillingService(this);
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

package org.guanpu.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;

public class AzureBillingUsageService {
	private final String ROOT_URL = "https://management.azure.com";
	private final String SUBSCRIPTION_PATH = "subscriptions";
	private final String USAGE_RESOURCE_PATH = "providers/Microsoft.Commerce/UsageAggregates";
	private final String API_VERSION_KET = "api-version";
	private final String USAGE_API_VERSION = "2015-06-01-preview";
	
	private final String subscription;
	private final String reportedStartTime;
	private final String reportedEndTime;
	private final String aggregationGranularity;
	private final boolean showDetails;
	private final String accessToken;
	
	private AzureBillingUsageService(Builder builder){
		this.subscription = builder.subscription;
		this.reportedStartTime = builder.reportedStartTime;
		this.reportedEndTime = builder.reportedEndTime;
		this.aggregationGranularity = builder.aggregationGranularity;
		this.showDetails = builder.showDetails;
		this.accessToken = builder.accessToken;
	}
	
	public Response invoke(){
		Client client = ClientBuilder.newClient();
		String authorization = "Bearer " + accessToken;
		
		WebTarget webTarget = client.target(ROOT_URL).path(SUBSCRIPTION_PATH).path(subscription).path(USAGE_RESOURCE_PATH)
					.queryParam(API_VERSION_KET, USAGE_API_VERSION)
					.queryParam("reportedStartTime", reportedStartTime)
					.queryParam("reportedEndTime", reportedEndTime)
					.queryParam("aggregationGranularity", aggregationGranularity)
					.queryParam("showDetails", showDetails);
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON)
														 .header(HttpHeaders.AUTHORIZATION, authorization);
		Response response = invocationBuilder.get();
		
		return response;
	}
	
	public static final class Builder {

		private String subscription;
		private String reportedStartTime;
		private String reportedEndTime;
		private String aggregationGranularity;
		private boolean showDetails;
		private String accessToken;
		
		public AzureBillingUsageService build(){
			Preconditions.checkNotNull(subscription, "subscription can't be null");
			Preconditions.checkNotNull(reportedStartTime, "reportedStartTime can't be null");
			Preconditions.checkNotNull(reportedEndTime, "reportedEndTime can't be null");
			Preconditions.checkNotNull(aggregationGranularity, "aggregationGranularity can't be null");
			Preconditions.checkNotNull(showDetails, "showDetails can't be null");
			Preconditions.checkNotNull(accessToken, "accessToken can't be null");
			return new AzureBillingUsageService(this);
		}
		
		public Builder setAccessToken(String accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		public Builder setSubscription(String subscription) {
			this.subscription = subscription;
			return this;
		}

		public Builder setReportedStartTime(String reportedStartTime) {
			this.reportedStartTime = reportedStartTime;
			return this;
		}

		public Builder setReportedEndTime(String reportedEndTime) {
			this.reportedEndTime = reportedEndTime;
			return this;
		}

		public Builder setAggregationGranularity(String aggregationGranularity) {
			this.aggregationGranularity = aggregationGranularity;
			return this;
		}

		public Builder setShowDetails(boolean showDetails) {
			this.showDetails = showDetails;
			return this;
		}
		
	}
}

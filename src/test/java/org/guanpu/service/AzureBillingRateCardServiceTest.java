package org.guanpu.service;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.guanpu.core.AzureCredential;
import org.guanpu.vo.Credential;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AzureBillingRateCardServiceTest {

	AzureCredential credential;
	AzureBillingRateCardService service;
	Credential credentialVO;
	Response azureCredentialResponse;
	Response azureBillingServiceResponse;
	
	@Before
	public void setUp() throws Exception {
		credential = new AzureCredential.Builder()
			.setClientId("e4ec2c26-7b68-4b31-9665-b6cdeceb78a3")
			.setTenant("472613e3-303b-4ae2-afc6-6a3b2d920675")
			.setClientSecret("tD0HJEdnovFZ9ytAINVsDZnriebhLZuGtrv46W2y0g8=")
			.build();
	
		azureCredentialResponse = credential.invoke();
		
		credentialVO = new ObjectMapper()
		  .readerFor(Credential.class)
		  .readValue(azureCredentialResponse.readEntity(String.class));
		
		service = new AzureBillingRateCardService.Builder()
			.setAccessToken(credentialVO.getAccessToken())
			.setSubscription("08baa038-b64f-49f0-a084-7c22d1c1305c")
			.setOfferDurableId("MS-AZR-0025P")
			.setCurrency("TWD")
			.setLocale("zh-TW")
			.setRegionInfo("TW")
			.build();
		
		azureBillingServiceResponse = service.invoke();
	}

	@After
	public void tearDown() throws Exception {
		azureCredentialResponse.close();
		azureBillingServiceResponse.close();
	}

	@Test
	public void testRateCard() {
		assertEquals(200, azureBillingServiceResponse.getStatus());
	}

}

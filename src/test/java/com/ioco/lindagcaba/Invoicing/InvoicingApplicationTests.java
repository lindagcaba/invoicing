package com.ioco.lindagcaba.Invoicing;

import com.ioco.lindagcaba.Invoicing.model.Invoice;
import com.ioco.lindagcaba.Invoicing.model.LineItem;
import com.ioco.lindagcaba.Invoicing.repository.InvoiceRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class InvoicingApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private InvoiceRepository invoiceRepository;

	@BeforeEach
	void cleanUp(){
		invoiceRepository.deleteAll();
	}
	@Test
	public void testViewAllInvoices(){
		ResponseEntity<String> response = testRestTemplate.getForEntity("/invoices",String.class);
		assertEquals(200,response.getStatusCodeValue());
	}

	@Test
	public void testAddInvoice() {
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(200,response.getStatusCodeValue());
		assertTrue(!response.getBody().isEmpty());
		System.out.println("System Out body");
		System.out.println(response.getBody());
	}
	@Test
	public void testViewInvoice() throws URISyntaxException,JSONException{
		Invoice invoice = new Invoice("SampleCo",LocalDate.parse("2019-11-22"),15L);
		LineItem lineItem = new LineItem("Cloud Server Costs", new BigDecimal( 560.36),3L);

		invoice.addLineItem(lineItem);
		invoiceRepository.save(invoice);
		Long invoiceId = invoice.getId();
		Long lineItemId = invoice.getLineItems().stream().findFirst().get().getId();
		 String url = "http://localhost:"+port+"/invoices/"+ invoice.getId();
		ResponseEntity<String> response = testRestTemplate.getForEntity(new URI(url),String.class);
		String expectedResponseBody ="  {\n" +
				"        \"id\":" + invoiceId + ",\n" +
				"        \"client\": \"SampleCo\",\n" +
				"        \"invoiceDate\": \"2019-11-22\",\n" +
				"        \"vatRate\": 15,\n" +
				"        \"lineItems\": [\n" +
				"            {\n" +
				"                \"id\":" + lineItemId + ",\n" +
				"                \"quantity\": 3,\n" +
				"                \"description\": \"Cloud Server Costs\",\n" +
				"                \"unitPrice\": 560.36,\n" +
				"                \"lineItemTotal\": 1681.08\n" +
				"            }\n" +
				"        ],\n" +
				"        \"total\": 1933.24,\n" +
				"        \"subTotal\": 1681.08,\n" +
				"        \"vat\": 252.16\n" +
				"    }";

		assertEquals(200,response.getStatusCodeValue());
		JSONAssert.assertEquals(expectedResponseBody,response.getBody(),true);
	}

	@Test
	public void testAddInvoiceInvalidRequestEmptyClient(){
		String requestJSON = "{ \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());

	}

	@Test
	public void testAddInvoiceInvalidRequestNoDate(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}

	@Test
	public void testAddInvoiceInvalidRequestNegativeVatRate(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": -15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());

	}

	@Test
	public void testAddInvoiceInvalidRequestNoLineItems(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": -15,\n" +
		        "Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}

	@Test
	public void testAddInvoiceInvalidRequestInvalidLineItemNoQuantity(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}

	@Test
	public void testAddInvoiceInvalidRequestInvalidLineItemNegativeQuantity(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": -8,\"description\": \"Cloud services Consulting\",\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}

	@Test
	public void testAddInvoiceInvalidRequestInvalidLineItemNoDescription(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": 560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}
	@Test
	public void testAddInvoiceInvalidRequestInvalidLineItemNoUnitPrice(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \"}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}
	@Test
	public void testAddInvoiceInvalidRequestInvalidLineItemInvalidUnitPrice(){
		String requestJSON = "{ \"client\":\"Some BlueChip\",\n" +
				"  \"invoiceDate\": \"2019-11-22\",\n" +
				"  \"vatRate\": 15,\n" +
				"  \"lineItems\": [{\"quantity\": 8,\"unitPrice\": 750},{\"quantity\": 3,\"description\": \"Clousd Server Costs Ocean \",\"unitPrice\": -560.36}]\n" +
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = 	new HttpEntity<>(requestJSON,headers);
		ResponseEntity<String> response = testRestTemplate.exchange("/invoices",HttpMethod.POST,request,String.class);
		assertEquals(400,response.getStatusCodeValue());
	}
}

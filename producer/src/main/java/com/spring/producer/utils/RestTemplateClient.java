package com.spring.producer.utils;

import com.spring.producer.dto.WebTokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

@Component
public class RestTemplateClient {
    private final Logger log = LoggerFactory.getLogger(RestTemplateClient.class);

    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;

    public RestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    public ResponseEntity<String> get(String endPoint, Map<String, String> queryParameters, Map<String, String> headerParams) {
        ResponseEntity<String> jsonResponse = null;
        URI requestUrl = buildUrl(endPoint, queryParameters);

        if (log.isDebugEnabled()) {
            log.debug("Making GET request to {}", requestUrl);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach((K, V) -> headers.set(K, V));
            }
            jsonResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

            if (jsonResponse != null) {
                switch (jsonResponse.getStatusCodeValue()) {
                    case 200:
                        if (jsonResponse.getBody() != null)
                            log.info("Successful service call {}", requestUrl);
                        else
                            log.info("Empty response from service {}", requestUrl);
                        break;
                    default:
                        throw new Exception("Unexpected status code: " + jsonResponse.getStatusCodeValue() + " -- request url: " + requestUrl);
                }
            }
        } catch (Throwable ex) {
            log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
            jsonResponse = new ResponseEntity<>("{\"response\": \"Request failed. API encountered a problem.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return jsonResponse;
    }

    public ResponseEntity<String> head(String endPoint, Map<String, String> queryParameters, Map<String, String> headerParams) {
        ResponseEntity<String> jsonResponse = null;
        URI requestUrl = buildUrl(endPoint, queryParameters);

        if (log.isDebugEnabled()) {
            log.debug("Making HEAD request to {}", requestUrl);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach((K, V) -> headers.set(K, V));
            }
            jsonResponse = restTemplate.exchange(requestUrl, HttpMethod.HEAD, new HttpEntity<Object>(headers), String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            log.debug("Error accessing service endpoint: {} error: {} -- {}", endPoint, httpClientErrorException.getMessage(), httpClientErrorException.getStackTrace());
            jsonResponse = new ResponseEntity<>("{\"response\": \"Request failed. API encountered a problem.\"}", httpClientErrorException.getStatusCode());
        } catch (Throwable ex) {
            log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
            jsonResponse = new ResponseEntity<>("{\"response\": \"Request failed. API encountered a problem.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return jsonResponse;
    }

    public ResponseEntity<String> put(String endPoint, Map<String, String> queryParameters, Map<String, String> headerParams) {
        ResponseEntity<String> jsonResponse = null;
        URI requestUrl = buildUrl(endPoint, queryParameters);

        if (log.isDebugEnabled()) {
            log.debug("Making PUT request to {}", requestUrl);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach((K, V) -> headers.set(K, V));
            }
            jsonResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, new HttpEntity<Object>(headers), String.class);

            if (jsonResponse != null) {
                switch (jsonResponse.getStatusCodeValue()) {
                    case 200:
                        if (jsonResponse.getBody() != null)
                            log.info("Successful service call {}", requestUrl);
                        else
                            log.info("Empty response from service {}", requestUrl);
                        break;
                    default:
                        throw new Exception("Unexpected status code: " + jsonResponse.getStatusCodeValue() + " -- request url: " + requestUrl);
                }
            }
        } catch (Throwable ex) {
            log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
            jsonResponse = new ResponseEntity<>("{\"response\": \"Request failed. API encountered a problem.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return jsonResponse;
    }

    public ResponseEntity<String> post(String endPoint, Object object, Map<String, String> queryParameters) {
        URI requestUrl = buildUrl(endPoint, queryParameters);

        if (log.isDebugEnabled())
            log.debug("Making POST request to {}", requestUrl);

        ResponseEntity<String> tmp = restTemplate.exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(object, httpHeaders), String.class);

        return new ResponseEntity<>(tmp.getBody(), tmp.getStatusCode());
    }

    public ResponseEntity<String> post(String endPoint, Object object, Map<String, String> queryParameters, Map<String, String> headerParams) {
        URI requestUrl = buildUrl(endPoint, queryParameters);

        if (log.isDebugEnabled())
            log.debug("Making POST request to {}", requestUrl);
        HttpHeaders headers = new HttpHeaders();
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach((K, V) -> headers.set(K, V));
        }
        ResponseEntity<String> tmp = null;
        try {
            tmp = restTemplate.exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(object, headers), String.class);
        } catch (Exception ex) {
            log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
        }
        if (!tmp.getStatusCode().is2xxSuccessful())
            log.debug(tmp.getBody());

        return new ResponseEntity<>(tmp.getBody(), tmp.getStatusCode());
    }

    public ResponseEntity<WebTokenDto> postFormData(String endPoint, MultiValueMap<String, String> queryParameters) {
        ResponseEntity<WebTokenDto> jsonResponse = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(queryParameters, headers);

            jsonResponse = restTemplate.postForEntity(endPoint, request, WebTokenDto.class);
        } catch (
                HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("No result were found. Response from service {}", endPoint);
            } else {
                log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
                throw ex;
            }
        } catch (RestClientException ex) {
            log.error("Error accessing service endpoint: {} error: {} -- {}", endPoint, ex.getMessage(), ex.getStackTrace());
            throw ex;
        }
        return jsonResponse;

    }

    private static URI buildUrl(String endPoint, Map<String, String> queryParameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endPoint);
        if (!CollectionUtils.isEmpty(queryParameters)) {
            queryParameters.forEach((K, V) -> builder.queryParam(K, V));
        }
        return builder.build().toUri();
    }
}

package com.service.consumer.controllers;

import com.service.consumer.entities.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/users")
public class UserController {

    private final RestTemplate restTemplate;

    @Value("${PRODUCER_URL}")
    private String serviceUrl;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "Endpoint to get the user with a specified id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Not Found.")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getAccountInfo(
            @PathVariable("userId") String userId
    ) throws ExecutionException, InterruptedException {
        try {
            String url = String.format("%s/health/count/%s", serviceUrl, userId);
            ResponseEntity<UserEntity> userResponseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            UserEntity wrapper = userResponseEntity.getBody();

            if (wrapper == null || wrapper.equals(new UserEntity())) {
                return (ResponseEntity<UserEntity>) ResponseEntity.noContent();
            }

            UserEntity accountInfo = wrapper;
            return ResponseEntity.ok(accountInfo);
        }
        catch (
                HttpServerErrorException e) {
                return (ResponseEntity<UserEntity>) ResponseEntity.internalServerError();
        }
    }
}

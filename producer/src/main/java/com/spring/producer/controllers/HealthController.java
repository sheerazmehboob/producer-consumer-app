package com.spring.producer.controllers;

import com.spring.producer.dto.ApiResponseDTO;
import com.spring.producer.entities.CountRequests;
import com.spring.producer.repositories.CountRequestsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Operation(summary = "Endpoint to check Health")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Not Found.")
    })
    @GetMapping("check")
    public ResponseEntity<ApiResponseDTO<String>> getAccountInfo(
    ) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(new ApiResponseDTO<>(HttpStatus.OK.value(), "Success", null));
    }


    CountRequestsRepository countRequestsRepository;
    @Autowired
    public HealthController(CountRequestsRepository countRequestsRepository) {
        this.countRequestsRepository = countRequestsRepository;
    }

    @Operation(summary = "Endpoint to count requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Not Found.")
    })

    @PostMapping("/count")
    public ApiResponseDTO<?> saveAccountInfo(
            @RequestBody CountRequests countRequests
    ) throws ExecutionException, InterruptedException {
        countRequestsRepository.save(countRequests);
        return new ApiResponseDTO<CountRequests>(200, "Success", countRequests);
    }

    @GetMapping("/count/{userId}")
    public Optional<CountRequests> saveAccountInfo(
            @PathVariable("userId") String userId
    ) throws ExecutionException, InterruptedException {
        Optional<CountRequests> countRequest = countRequestsRepository.findById(Long.valueOf(userId));
        return countRequest;
    }

}

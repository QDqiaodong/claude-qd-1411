package com.tea.primary.farmer;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {
    private final FarmerService service;

    public FarmerController(FarmerService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<Farmer>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(service.list(keyword));
    }

    @GetMapping("/{id}")
    public ApiResponse<Farmer> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<Farmer> create(@RequestBody Farmer request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Farmer> update(@PathVariable Long id, @RequestBody Farmer request) {
        return ApiResponse.success(service.update(id, request));
    }
}

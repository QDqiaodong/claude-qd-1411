package com.tea.primary.inventory;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/finished-inventory")
public class FinishedInventoryController {
    private final FinishedInventoryService service;

    public FinishedInventoryController(FinishedInventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<FinishedInventory>> list(@RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) String grade,
                                                     @RequestParam(required = false) String storageLocation) {
        return ApiResponse.success(service.list(keyword, grade, storageLocation));
    }

    @GetMapping("/{id}")
    public ApiResponse<FinishedInventory> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<FinishedInventory> create(@RequestBody FinishedInventory request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<FinishedInventory> update(@PathVariable Long id, @RequestBody FinishedInventory request) {
        return ApiResponse.success(service.update(id, request));
    }
}

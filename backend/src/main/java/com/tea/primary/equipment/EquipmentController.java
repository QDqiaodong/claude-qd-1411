package com.tea.primary.equipment;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<Equipment>> list(@RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) String status) {
        return ApiResponse.success(service.list(keyword, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<Equipment> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<Equipment> create(@RequestBody Equipment request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Equipment> update(@PathVariable Long id, @RequestBody Equipment request) {
        return ApiResponse.success(service.update(id, request));
    }
}

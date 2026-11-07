package com.tea.primary.purchase;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<PurchaseOrder>> list(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Long farmerId,
                                                 @RequestParam(required = false) Long plotId) {
        return ApiResponse.success(service.list(keyword, farmerId, plotId));
    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrder> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<PurchaseOrder> create(@RequestBody PurchaseOrder request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PurchaseOrder> update(@PathVariable Long id, @RequestBody PurchaseOrder request) {
        return ApiResponse.success(service.update(id, request));
    }
}

package com.tea.primary.processing;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/processing-batches")
public class ProcessingBatchController {
    private final ProcessingBatchService service;

    public ProcessingBatchController(ProcessingBatchService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<ProcessingBatch>> list(@RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String currentProcess,
                                                   @RequestParam(required = false) String batchStatus) {
        return ApiResponse.success(service.list(keyword, currentProcess, batchStatus));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProcessingBatch> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<ProcessingBatch> create(@RequestBody ProcessingBatch request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProcessingBatch> update(@PathVariable Long id, @RequestBody ProcessingBatch request) {
        return ApiResponse.success(service.update(id, request));
    }
}

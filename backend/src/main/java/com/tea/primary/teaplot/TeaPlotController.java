package com.tea.primary.teaplot;

import com.tea.primary.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tea-plots")
public class TeaPlotController {
    private final TeaPlotService service;

    public TeaPlotController(TeaPlotService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<TeaPlot>> list(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Long farmerId) {
        return ApiResponse.success(service.list(keyword, farmerId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TeaPlot> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<TeaPlot> create(@RequestBody TeaPlot request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<TeaPlot> update(@PathVariable Long id, @RequestBody TeaPlot request) {
        return ApiResponse.success(service.update(id, request));
    }
}

package com.tea.primary.teaplot;

import com.tea.primary.common.BizException;
import com.tea.primary.common.Dict;
import com.tea.primary.farmer.FarmerRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeaPlotService {
    private final TeaPlotRepository repository;
    private final FarmerRepository farmerRepository;

    public TeaPlotService(TeaPlotRepository repository, FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<TeaPlot> list(String keyword, Long farmerId) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> (farmerId == null || farmerId.equals(item.getFarmerId()))
                && (q.isEmpty() || item.getPlotName().toLowerCase().contains(q)
                || item.getPickingSeason().toLowerCase().contains(q))).toList();
    }

    public TeaPlot detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("茶园地块不存在"));
    }

    public TeaPlot create(TeaPlot request) {
        validate(request);
        TeaPlot item = new TeaPlot();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public TeaPlot update(Long id, TeaPlot request) {
        validate(request);
        TeaPlot item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(TeaPlot request) {
        if (request.getFarmerId() == null || !farmerRepository.existsById(request.getFarmerId())) throw new BizException("请选择有效农户");
        if (request.getPlotName() == null || request.getPlotName().isBlank()) throw new BizException("地块名不能为空");
        if (request.getAreaMu() == null || request.getAreaMu().signum() <= 0) throw new BizException("面积必须大于 0");
        if (request.getAltitudeM() == null || request.getAltitudeM() <= 0) throw new BizException("海拔必须大于 0");
        if (!Dict.contains(Dict.PICKING_SEASONS, request.getPickingSeason())) throw new BizException("采摘季不在字典范围内");
    }

    private void copy(TeaPlot source, TeaPlot target) {
        target.setFarmerId(source.getFarmerId());
        target.setPlotName(source.getPlotName());
        target.setAreaMu(source.getAreaMu());
        target.setAltitudeM(source.getAltitudeM());
        target.setPickingSeason(source.getPickingSeason());
    }
}

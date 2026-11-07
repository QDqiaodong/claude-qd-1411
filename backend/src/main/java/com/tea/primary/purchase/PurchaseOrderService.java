package com.tea.primary.purchase;

import com.tea.primary.common.BizException;
import com.tea.primary.farmer.FarmerRepository;
import com.tea.primary.teaplot.TeaPlot;
import com.tea.primary.teaplot.TeaPlotRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;
    private final FarmerRepository farmerRepository;
    private final TeaPlotRepository plotRepository;

    public PurchaseOrderService(PurchaseOrderRepository repository, FarmerRepository farmerRepository,
                                TeaPlotRepository plotRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.plotRepository = plotRepository;
    }

    public List<PurchaseOrder> list(String keyword, Long farmerId, Long plotId) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> (farmerId == null || farmerId.equals(item.getFarmerId()))
                && (plotId == null || plotId.equals(item.getPlotId()))
                && (q.isEmpty() || item.getOrderNo().toLowerCase().contains(q)
                || item.getPurchaser().toLowerCase().contains(q))).toList();
    }

    public PurchaseOrder detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("鲜叶收购单不存在"));
    }

    public PurchaseOrder create(PurchaseOrder request) {
        validate(request);
        PurchaseOrder item = new PurchaseOrder();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public PurchaseOrder update(Long id, PurchaseOrder request) {
        validate(request);
        PurchaseOrder item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(PurchaseOrder request) {
        if (request.getOrderNo() == null || request.getOrderNo().isBlank()) throw new BizException("收购单号不能为空");
        if (request.getFarmerId() == null || !farmerRepository.existsById(request.getFarmerId())) throw new BizException("请选择有效农户");
        TeaPlot plot = request.getPlotId() == null ? null : plotRepository.findById(request.getPlotId()).orElse(null);
        if (plot == null) throw new BizException("请选择有效地块");
        if (!request.getFarmerId().equals(plot.getFarmerId())) throw new BizException("所选地块不属于该农户");
        if (request.getWeightKg() == null || request.getWeightKg().signum() <= 0) throw new BizException("鲜叶重量必须大于 0");
        if (request.getMoisturePercent() == null || request.getMoisturePercent().signum() < 0
                || request.getMoisturePercent().compareTo(new java.math.BigDecimal("100")) > 0) throw new BizException("含水率应在 0 到 100 之间");
        if (request.getPickedAt() == null || request.getPurchaseDate() == null) throw new BizException("采摘时间和收购日期不能为空");
        if (request.getPurchaser() == null || request.getPurchaser().isBlank()) throw new BizException("收购人不能为空");
    }

    private void copy(PurchaseOrder source, PurchaseOrder target) {
        target.setOrderNo(source.getOrderNo());
        target.setFarmerId(source.getFarmerId());
        target.setPlotId(source.getPlotId());
        target.setWeightKg(source.getWeightKg());
        target.setMoisturePercent(source.getMoisturePercent());
        target.setPickedAt(source.getPickedAt());
        target.setPurchaser(source.getPurchaser());
        target.setPurchaseDate(source.getPurchaseDate());
    }
}

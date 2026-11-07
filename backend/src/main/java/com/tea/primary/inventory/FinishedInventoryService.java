package com.tea.primary.inventory;

import com.tea.primary.common.BizException;
import com.tea.primary.common.Dict;
import com.tea.primary.processing.ProcessingBatchRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinishedInventoryService {
    private final FinishedInventoryRepository repository;
    private final ProcessingBatchRepository batchRepository;

    public FinishedInventoryService(FinishedInventoryRepository repository, ProcessingBatchRepository batchRepository) {
        this.repository = repository;
        this.batchRepository = batchRepository;
    }

    public List<FinishedInventory> list(String keyword, String grade, String storageLocation) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> (grade == null || grade.equals(item.getGrade()))
                && (storageLocation == null || storageLocation.equals(item.getStorageLocation()))
                && (q.isEmpty() || item.getProductBatchNo().toLowerCase().contains(q))).toList();
    }

    public FinishedInventory detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("成品库存记录不存在"));
    }

    public FinishedInventory create(FinishedInventory request) {
        validate(request);
        FinishedInventory item = new FinishedInventory();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public FinishedInventory update(Long id, FinishedInventory request) {
        validate(request);
        FinishedInventory item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(FinishedInventory request) {
        if (request.getProductBatchNo() == null || request.getProductBatchNo().isBlank()) throw new BizException("成品批次不能为空");
        if (request.getProcessingBatchId() == null || !batchRepository.existsById(request.getProcessingBatchId())) throw new BizException("请选择有效加工批次");
        if (!Dict.contains(Dict.PRODUCT_GRADES, request.getGrade())) throw new BizException("成品等级不在字典范围内");
        if (request.getWeightKg() == null || request.getWeightKg().signum() <= 0) throw new BizException("成品重量必须大于 0");
        if (request.getStockedAt() == null) throw new BizException("入库时间不能为空");
        if (request.getStorageLocation() == null || request.getStorageLocation().isBlank()) throw new BizException("库位不能为空");
    }

    private void copy(FinishedInventory source, FinishedInventory target) {
        target.setProductBatchNo(source.getProductBatchNo());
        target.setProcessingBatchId(source.getProcessingBatchId());
        target.setGrade(source.getGrade());
        target.setWeightKg(source.getWeightKg());
        target.setStockedAt(source.getStockedAt());
        target.setStorageLocation(source.getStorageLocation());
    }
}

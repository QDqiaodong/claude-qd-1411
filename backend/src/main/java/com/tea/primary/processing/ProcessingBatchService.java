package com.tea.primary.processing;

import com.tea.primary.common.BizException;
import com.tea.primary.common.Dict;
import com.tea.primary.purchase.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProcessingBatchService {
    private final ProcessingBatchRepository repository;
    private final PurchaseOrderRepository purchaseRepository;

    public ProcessingBatchService(ProcessingBatchRepository repository, PurchaseOrderRepository purchaseRepository) {
        this.repository = repository;
        this.purchaseRepository = purchaseRepository;
    }

    public List<ProcessingBatch> list(String keyword, String currentProcess, String batchStatus) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> (currentProcess == null || currentProcess.equals(item.getCurrentProcess()))
                && (batchStatus == null || batchStatus.equals(item.getBatchStatus()))
                && (q.isEmpty() || item.getBatchNo().toLowerCase().contains(q))).toList();
    }

    public ProcessingBatch detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("加工批次不存在"));
    }

    public ProcessingBatch create(ProcessingBatch request) {
        validate(request);
        ProcessingBatch item = new ProcessingBatch();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public ProcessingBatch update(Long id, ProcessingBatch request) {
        validate(request);
        ProcessingBatch item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(ProcessingBatch request) {
        if (request.getBatchNo() == null || request.getBatchNo().isBlank()) throw new BizException("加工批次号不能为空");
        if (request.getPurchaseOrderId() == null || !purchaseRepository.existsById(request.getPurchaseOrderId())) throw new BizException("请选择有效收购单");
        if (!Dict.contains(Dict.PROCESS_STEPS, request.getCurrentProcess())) throw new BizException("当前工序不在字典范围内");
        if (!Dict.contains(Dict.BATCH_STATUS, request.getBatchStatus())) throw new BizException("批次状态不在字典范围内");
    }

    private void copy(ProcessingBatch source, ProcessingBatch target) {
        target.setBatchNo(source.getBatchNo());
        target.setPurchaseOrderId(source.getPurchaseOrderId());
        target.setCurrentProcess(source.getCurrentProcess());
        target.setBatchStatus(source.getBatchStatus());
    }
}

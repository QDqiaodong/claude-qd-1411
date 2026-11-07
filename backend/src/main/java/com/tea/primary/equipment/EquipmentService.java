package com.tea.primary.equipment;

import com.tea.primary.common.BizException;
import com.tea.primary.common.Dict;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    public List<Equipment> list(String keyword, String status) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> (status == null || status.equals(item.getStatus()))
                && (q.isEmpty() || item.getEquipmentName().toLowerCase().contains(q)
                || item.getEquipmentNo().toLowerCase().contains(q))).toList();
    }

    public Equipment detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("设备不存在"));
    }

    public Equipment create(Equipment request) {
        validate(request);
        Equipment item = new Equipment();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public Equipment update(Long id, Equipment request) {
        validate(request);
        Equipment item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(Equipment request) {
        if (request.getEquipmentName() == null || request.getEquipmentName().isBlank()) throw new BizException("设备名不能为空");
        if (request.getEquipmentNo() == null || request.getEquipmentNo().isBlank()) throw new BizException("设备编号不能为空");
        if (!Dict.contains(Dict.EQUIPMENT_STATUS, request.getStatus())) throw new BizException("设备状态不在字典范围内");
        if (request.getLastMaintenanceDate() == null) throw new BizException("上次保养日期不能为空");
    }

    private void copy(Equipment source, Equipment target) {
        target.setEquipmentName(source.getEquipmentName());
        target.setEquipmentNo(source.getEquipmentNo());
        target.setStatus(source.getStatus());
        target.setLastMaintenanceDate(source.getLastMaintenanceDate());
    }
}

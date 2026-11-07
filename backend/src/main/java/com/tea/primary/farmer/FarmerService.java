package com.tea.primary.farmer;

import com.tea.primary.common.BizException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmerService {
    private final FarmerRepository repository;

    public FarmerService(FarmerRepository repository) {
        this.repository = repository;
    }

    public List<Farmer> list(String keyword) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return repository.findAll().stream().filter(item -> q.isEmpty()
                || contains(item.getName(), q) || contains(item.getPhone(), q)
                || contains(item.getVillageGroup(), q)).toList();
    }

    public Farmer detail(Long id) {
        return repository.findById(id).orElseThrow(() -> new BizException("农户不存在"));
    }

    public Farmer create(Farmer request) {
        validate(request);
        Farmer item = new Farmer();
        copy(request, item);
        item.setCreatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    public Farmer update(Long id, Farmer request) {
        validate(request);
        Farmer item = detail(id);
        copy(request, item);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    private void validate(Farmer request) {
        if (request.getName() == null || request.getName().isBlank()) throw new BizException("农户姓名不能为空");
        if (request.getPhone() == null || request.getPhone().isBlank()) throw new BizException("联系电话不能为空");
        if (request.getVillageGroup() == null || request.getVillageGroup().isBlank()) throw new BizException("村组不能为空");
    }

    private void copy(Farmer source, Farmer target) {
        target.setName(source.getName());
        target.setPhone(source.getPhone());
        target.setVillageGroup(source.getVillageGroup());
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}

package com.tea.primary.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FinishedInventoryRepository extends JpaRepository<FinishedInventory, Long> {
}

SET NAMES utf8mb4;

DROP TABLE IF EXISTS finished_inventory;
DROP TABLE IF EXISTS processing_batch;
DROP TABLE IF EXISTS purchase_order;
DROP TABLE IF EXISTS tea_plot;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS farmer;

CREATE TABLE farmer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(30) NOT NULL UNIQUE,
    village_group VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tea_plot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    farmer_id BIGINT NOT NULL,
    plot_name VARCHAR(80) NOT NULL,
    area_mu DECIMAL(10,2) NOT NULL,
    altitude_m INT NOT NULL,
    picking_season VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT fk_plot_farmer FOREIGN KEY (farmer_id) REFERENCES farmer(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE purchase_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(40) NOT NULL UNIQUE,
    farmer_id BIGINT NOT NULL,
    plot_id BIGINT NOT NULL,
    weight_kg DECIMAL(10,2) NOT NULL,
    moisture_percent DECIMAL(5,2) NOT NULL,
    picked_at DATETIME NOT NULL,
    purchaser VARCHAR(50) NOT NULL,
    purchase_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT fk_purchase_farmer FOREIGN KEY (farmer_id) REFERENCES farmer(id),
    CONSTRAINT fk_purchase_plot FOREIGN KEY (plot_id) REFERENCES tea_plot(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE processing_batch (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    batch_no VARCHAR(40) NOT NULL UNIQUE,
    purchase_order_id BIGINT NOT NULL,
    current_process VARCHAR(20) NOT NULL,
    batch_status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT fk_batch_purchase FOREIGN KEY (purchase_order_id) REFERENCES purchase_order(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE finished_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_batch_no VARCHAR(40) NOT NULL UNIQUE,
    processing_batch_id BIGINT NOT NULL,
    grade VARCHAR(20) NOT NULL,
    weight_kg DECIMAL(10,2) NOT NULL,
    stocked_at DATETIME NOT NULL,
    storage_location VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT fk_inventory_batch FOREIGN KEY (processing_batch_id) REFERENCES processing_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_name VARCHAR(50) NOT NULL,
    equipment_no VARCHAR(40) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    last_maintenance_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO farmer (name, phone, village_group, created_at) VALUES
('陈兴国', '13870010001', '云岭村一组', '2026-10-20 08:00:00'),
('周明秀', '13870010002', '云岭村三组', '2026-10-20 08:10:00'),
('王守田', '13870010003', '青峰村二组', '2026-10-20 08:20:00'),
('李春梅', '13870010004', '青峰村五组', '2026-10-20 08:30:00'),
('赵德安', '13870010005', '石坎村四组', '2026-10-20 08:40:00');

INSERT INTO tea_plot (farmer_id, plot_name, area_mu, altitude_m, picking_season, created_at) VALUES
(1, '云岭东坡', 12.50, 980, '春季', '2026-10-20 09:00:00'),
(2, '老茶湾', 8.20, 1120, '春季', '2026-10-20 09:10:00'),
(3, '青峰南坳', 15.80, 860, '夏季', '2026-10-20 09:20:00'),
(4, '竹林坡', 9.60, 1050, '秋季', '2026-10-20 09:30:00'),
(5, '石坎高山园', 18.40, 1260, '春季', '2026-10-20 09:40:00');

INSERT INTO purchase_order (order_no, farmer_id, plot_id, weight_kg, moisture_percent, picked_at, purchaser, purchase_date, created_at) VALUES
('CG20261101001', 1, 1, 286.50, 74.20, '2026-11-01 06:20:00', '刘海', '2026-11-01', '2026-11-01 08:00:00'),
('CG20261101002', 2, 2, 198.00, 73.80, '2026-11-01 06:45:00', '刘海', '2026-11-01', '2026-11-01 08:15:00'),
('CG20261102001', 3, 3, 315.20, 75.10, '2026-11-02 06:10:00', '孙强', '2026-11-02', '2026-11-02 08:05:00'),
('CG20261102002', 4, 4, 226.80, 72.90, '2026-11-02 06:35:00', '孙强', '2026-11-02', '2026-11-02 08:20:00'),
('CG20261103001', 5, 5, 342.60, 74.60, '2026-11-03 06:15:00', '刘海', '2026-11-03', '2026-11-03 08:10:00');

INSERT INTO processing_batch (batch_no, purchase_order_id, current_process, batch_status, created_at) VALUES
('PC20261101A', 1, '烘干', '已完成', '2026-11-01 09:00:00'),
('PC20261101B', 2, '揉捻', '加工中', '2026-11-01 09:20:00'),
('PC20261102A', 3, '杀青', '加工中', '2026-11-02 09:00:00'),
('PC20261102B', 4, '萎凋', '待加工', '2026-11-02 09:30:00'),
('PC20261103A', 5, '烘干', '已完成', '2026-11-03 09:10:00');

INSERT INTO finished_inventory (product_batch_no, processing_batch_id, grade, weight_kg, stocked_at, storage_location, created_at) VALUES
('CP20261101A-01', 1, '特级', 42.50, '2026-11-01 18:20:00', 'A-01', '2026-11-01 18:20:00'),
('CP20261101A-02', 1, '一级', 31.80, '2026-11-01 18:25:00', 'A-02', '2026-11-01 18:25:00'),
('CP20261103A-01', 5, '特级', 48.20, '2026-11-03 18:40:00', 'A-03', '2026-11-03 18:40:00'),
('CP20261103A-02', 5, '一级', 36.60, '2026-11-03 18:45:00', 'B-01', '2026-11-03 18:45:00'),
('CP20261103A-03', 5, '二级', 22.40, '2026-11-03 18:50:00', 'B-02', '2026-11-03 18:50:00');

INSERT INTO equipment (equipment_name, equipment_no, status, last_maintenance_date, created_at) VALUES
('滚筒杀青机', 'SJ-001', '运行', '2026-10-18', '2026-10-20 10:00:00'),
('滚筒杀青机', 'SJ-002', '停机', '2026-10-12', '2026-10-20 10:10:00'),
('揉捻机', 'RN-001', '运行', '2026-10-22', '2026-10-20 10:20:00'),
('揉捻机', 'RN-002', '检修', '2026-09-28', '2026-10-20 10:30:00'),
('热风烘干机', 'HG-001', '运行', '2026-10-25', '2026-10-20 10:40:00');

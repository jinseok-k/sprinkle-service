-- -----------------------------------------------------
-- Table `hibernate_sequences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hibernate_sequences` (
    `sequence_name` VARCHAR(255) NOT NULL,
    `next_val` BIGINT(20) NULL,
    PRIMARY KEY (`sequence_name`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `sprinkle_info`(뿌리기 관리)
-- -----------------------------------------------------
CREATE TABLE `sprinkle_info` (
    `idx` BIGINT(20) NOT NULL COMMENT '뿌리기 인덱스',
    `user_id` BIGINT(20) NOT NULL COMMENT '사용자 아이디',
    `room_id` VARCHAR(64) NOT NULL COMMENT '대화방 아이디',
    `sprinkle_amount` INT(11) UNSIGNED NOT NULL COMMENT '뿌릴 금액',
    `sprinkle_count` INT(11) UNSIGNED NOT NULL COMMENT '뿌릴 인원',
    `reg_date` DATETIME NOT NULL COMMENT '등록 일시',
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT '뿌리기';

-- -----------------------------------------------------
-- Table `sprinkle_distribute`(뿌리기 분배)
-- -----------------------------------------------------
CREATE TABLE `sprinkle_distribute` (
    `idx` BIGINT(20) NOT NULL COMMENT '분배 인덱스',
    `sprinkle_idx` BIGINT(20) NOT NULL COMMENT '뿌리기 인덱스',
    `distribute_amount` INT(11) UNSIGNED NOT NULL COMMENT '분배 금액',
    PRIMARY KEY (`idx`),
    FOREIGN KEY (sprinkle_idx) REFERENCES sprinkle_info(idx)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT '뿌리기 분배';

-- -----------------------------------------------------
-- Table `sprinkle_pickup`(줍기)
-- -----------------------------------------------------
CREATE TABLE `sprinkle_pickup` (
    `distribute_idx` BIGINT(20) NOT NULL COMMENT '분배 인덱스',
    `user_id` BIGINT(20) NOT NULL COMMENT '사용자 아이디',
    `reg_date` DATETIME NOT NULL COMMENT '등록 일시',
    PRIMARY KEY (`distribute_idx`),
    FOREIGN KEY (distribute_idx) REFERENCES sprinkle_distribute(idx)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT '줍기';

-- -----------------------------------------------------
-- Table `token_info`(토큰 관리)
-- -----------------------------------------------------
CREATE TABLE `token_info` (
    `room_id` VARCHAR(64) NOT NULL COMMENT '대화방 아이디',
    `token` VARCHAR(3) NOT NULL COMMENT '토큰',
    `sprinkle_idx` BIGINT(20) NOT NULL COMMENT '뿌리기 인덱스',
    PRIMARY KEY (`room_id`, `token`),
    FOREIGN KEY (sprinkle_idx) REFERENCES sprinkle_info(idx)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT '토큰 관리';

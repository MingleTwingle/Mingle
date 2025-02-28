CREATE DATABASE mingle;  -- 데이터베이스 생성
USE mingle;              -- 데이터베이스 선택


drop table tbl_accommodation_Info;

-- tbl_accommodation_Info 테이블 생성
CREATE TABLE tbl_accommodation_Info (
    accommodation_id INT NOT NULL AUTO_INCREMENT COMMENT '숙소 아이디 시퀀스로 만들기',
    accommodation_name VARCHAR(20) NOT NULL COMMENT '숙소이름',
    accommodation_location VARCHAR(100) NULL COMMENT '숙소위치',
    accommodation_parking TINYINT NULL COMMENT '주차장이 있는지 없는지 여부',
    accommodation_morning TINYINT NULL COMMENT '조식여부를 나타내는 컬럼',
    accommodation_checkin_time DATETIME NULL COMMENT '체크인 시간을 나타내는 컬럼',
    PRIMARY KEY (accommodation_id)
);

-- tbl_restaurant_info 테이블 생성
CREATE TABLE `tbl_restaurant_info` (
    `restaurant_id` INT NOT NULL AUTO_INCREMENT,
    `restaurant_Name` VARCHAR(20) NULL,
    `restaurant_Location` VARCHAR(100) NULL,
    `restaurant_Parking` TINYINT  NULL,
    `reservation_Time` DATETIME NULL,
    `restaurant_openTime` TIME NULL,
    `restaurant_endTime` TIME NULL,
    PRIMARY KEY (`restaurant_id`)
);

-- tbl_accommodationRoom_info 테이블 생성
CREATE TABLE `tbl_accommodationRoom_info` (
    `accommodationRoom_Id` INT NOT NULL AUTO_INCREMENT COMMENT '숙소방아이디 시퀀스로 만들기',
    `accommodationRoom_name` VARCHAR(20) NULL COMMENT '방이름',
    `accommodationRoom_min` INT NULL COMMENT '사람 최소 인원',
    `accommodationRoom_max` INT NULL COMMENT '방 최대 인원',
    `accommodationRoom_cost` INT NULL COMMENT '가격',
    `accommodation_id` INT NOT NULL COMMENT '숙소 아이디 시퀀스로 만들기',
    PRIMARY KEY (`accommodationRoom_Id`),
    CONSTRAINT `FK_ACCOMMODATIONROOM_ACCOMMODATION`
        FOREIGN KEY (`accommodation_id`) REFERENCES `tbl_accommodation_Info`(`accommodation_id`)
);

-- tbl_restaurantmenu_info 테이블 생성
CREATE TABLE `tbl_restaurantmenu_info` (
    `restaurantmenu_id` INT NOT NULL AUTO_INCREMENT,
    `restaurantmenu_menu` VARCHAR(20) NULL,
    `restaurantmenu_category` VARCHAR(20) NULL,
    `restaurantmenu_cost` INT NULL,
    `restaurant_id` INT NOT NULL,
    PRIMARY KEY (`restaurantmenu_id`),
    CONSTRAINT `FK_RESTAURANTMENU_RESTAURANT`
        FOREIGN KEY (`restaurant_id`) REFERENCES `tbl_restaurant_info`(`restaurant_id`)
);

-- tbl_review 테이블 생성
CREATE TABLE `tbl_review` (
    `review_id` INT NOT NULL AUTO_INCREMENT COMMENT '시퀀스',
    `review_comment` VARCHAR(300) NOT NULL COMMENT '코멘트',
    `review_score` INT NOT NULL COMMENT '별점',
    `restaurant_id` INT NOT NULL,
    `accommodationRoom_Id` INT NOT NULL COMMENT '숙소방아이디 시퀀스로 만들기',
    `guest_key` INT NOT NULL,
    PRIMARY KEY (`review_id`),
    CONSTRAINT `FK_REVIEW_RESTAURANT`
        FOREIGN KEY (`restaurant_id`) REFERENCES `tbl_restaurant_info`(`restaurant_id`),
    CONSTRAINT `FK_REVIEW_ACCOMMODATIONROOM`
        FOREIGN KEY (`accommodationRoom_Id`) REFERENCES `tbl_accommodationRoom_info`(`accommodationRoom_Id`)
);

-- tbl_restaurant_photo 테이블 생성
CREATE TABLE `tbl_restaurant_photo` (
    `restaurantphoto_id` INT NOT NULL AUTO_INCREMENT,
    `restaurant_photo` LONGBLOB NULL,
    `restaurantmenu_id` INT NOT NULL,
    PRIMARY KEY (`restaurantphoto_id`),
    CONSTRAINT `FK_RESTAURANTPHOTO_RESTAURANTMENU`
        FOREIGN KEY (`restaurantmenu_id`) REFERENCES `tbl_restaurantmenu_info`(`restaurantmenu_id`)
);

-- tbl_reservation 테이블 생성
CREATE TABLE `tbl_reservation` (
    `reservation_id` INT NOT NULL AUTO_INCREMENT COMMENT '예약아이디 시퀀스로 만들기',
    `reservation_date` DATETIME NULL COMMENT '예약 날짜와 시간',
    `reservation_people` INT NULL COMMENT '예약한 인원수',
    `reservation_cancel` CHAR(1) NULL COMMENT '취소,변경,예약을 표기하는 컬럼',
    `restaurant_id` INT NOT NULL,
    `accommodationRoom_Id` INT NOT NULL COMMENT '숙소방아이디 시퀀스로 만들기',
    `guest_key` INT NOT NULL,
    PRIMARY KEY (`reservation_id`),
    CONSTRAINT `FK_RESERVATION_RESTAURANT`
        FOREIGN KEY (`restaurant_id`) REFERENCES `tbl_restaurant_info`(`restaurant_id`),
    CONSTRAINT `FK_RESERVATION_ACCOMMODATIONROOM`
        FOREIGN KEY (`accommodationRoom_Id`) REFERENCES `tbl_accommodationRoom_info`(`accommodationRoom_Id`)
);

-- tbl_accommodation_photo 테이블 생성
CREATE TABLE `tbl_accommodation_photo` (
    `accommodationPhoto_id` INT NOT NULL AUTO_INCREMENT COMMENT '사진 아이디 시퀀스',
    `accommodation_photo` LONGBLOB NULL COMMENT '사진',
    `accommodationRoom_Id` INT NOT NULL COMMENT '숙소방아이디 시퀀스로 만들기',
    PRIMARY KEY (`accommodationPhoto_id`)
);

-- tbl_guest 테이블 생성
CREATE TABLE `tbl_guest` (
    `guest_key` INT NOT NULL AUTO_INCREMENT,
    `guest_name` VARCHAR(20) NULL,
    `guest_idid` VARCHAR(15) NULL,
    `guest_password` VARCHAR(255) NULL,
    `guest_nickname` VARCHAR(5) NULL,
    `guest_email` VARCHAR(20) NULL,
    `guest_coupleCode` INT NULL,
    `guest_phoneNumber` VARCHAR(15) NULL,
    `guest_gender` VARCHAR(2) NULL,
    PRIMARY KEY (`guest_key`)
);

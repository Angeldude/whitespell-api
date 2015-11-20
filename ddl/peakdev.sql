CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(102) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `displayname` varchar(30) DEFAULT '',
  `registration_timestamp_utc` datetime DEFAULT NULL,
  `publisher` int(1) DEFAULT '0',
  `thumbnail` varchar(255) DEFAULT 'http://peakapp.me/img/app_assets/avatar.png',
  `rights` int(2) DEFAULT '0',
  `cover_photo` varchar(255) DEFAULT 'http://peakapp.me/img/app_assets/cover.png',
  `slogan` varchar(255) DEFAULT '',
  `newsfeed_processed` datetime DEFAULT NULL,
  `newsfeed_processing` datetime DEFAULT NULL,
  `email_verified` int(1) DEFAULT '0',
  `email_expiration` datetime DEFAULT NULL,
  `email_token` varchar(45) DEFAULT NULL,
  `reset_token` varchar(45) DEFAULT NULL,
  `fb_link` int(1) DEFAULT '0',
  `email_notifications` int(1) DEFAULT '1',
<<<<<<< HEAD
=======
  `fb_user_id` varchar(50) DEFAULT NULL,
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
<<<<<<< HEAD
  KEY `username_INDEX` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11955 DEFAULT CHARSET=utf8;CREATE TABLE `category` (
=======
  UNIQUE KEY `fb_user_id_UNIQUE` (`fb_user_id`),
  KEY `username_INDEX` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11975 DEFAULT CHARSET=utf8;CREATE TABLE `avcpvm_monitoring` (
  `instance_id` varchar(45) NOT NULL,
  `tasks_completed` int(11) DEFAULT '0',
  `queue_size` int(11) DEFAULT '0',
  `ipv4_address` varchar(45) DEFAULT NULL,
  `shutdown_reported` int(11) DEFAULT '0',
  `creation_time` varchar(45) DEFAULT NULL,
  `last_ping` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `category` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(45) DEFAULT NULL,
  `category_thumbnail` varchar(255) DEFAULT NULL,
  `category_followers` int(11) DEFAULT '0',
  `category_publishers` int(11) DEFAULT '0',
  PRIMARY KEY (`category_id`)
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;CREATE TABLE `content_type` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;CREATE TABLE `content_type` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `content_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `content_type_name` varchar(45) NOT NULL,
  PRIMARY KEY (`content_type_id`),
  UNIQUE KEY `content_type_name_UNIQUE` (`content_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;CREATE TABLE `content` (
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `content_type` int(2) DEFAULT '0',
<<<<<<< HEAD
  `content_url` varchar(255) DEFAULT NULL,
=======
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `content_title` varchar(45) DEFAULT NULL,
  `content_description` varchar(100) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT 'http://telecoms.com/wp-content/blogs.dir/1/files/2012/06/euro-football-sport.jpg',
  `content_likes` int(10) DEFAULT '0',
  `content_views` bigint(15) DEFAULT '0',
  `content_displays` bigint(15) DEFAULT '0',
  `content_comments` int(10) DEFAULT '0',
  `category_id` int(2) DEFAULT '1',
  `content_price` decimal(10,2) DEFAULT '0.00',
<<<<<<< HEAD
  `content_preview_720p` varchar(255) DEFAULT NULL,
  `content_url_1080p` varchar(255) DEFAULT NULL,
  `content_url_720p` varchar(255) DEFAULT NULL,
  `content_url_480p` varchar(255) DEFAULT NULL,
  `processed` int(1) NOT NULL DEFAULT '0',
  `parent` int(11) DEFAULT NULL,
=======
  `processed` int(1) NOT NULL DEFAULT '0',
  `content_url_1080p` varchar(255) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `content_url` varchar(255) DEFAULT NULL,
  `content_url_720p` varchar(255) DEFAULT NULL,
  `content_url_480p` varchar(255) DEFAULT NULL,
  `content_url_360p` varchar(255) DEFAULT NULL,
  `content_url_240p` varchar(255) DEFAULT NULL,
  `content_url_144p` varchar(255) DEFAULT NULL,
  `content_preview_1080p` varchar(255) DEFAULT NULL,
  `content_preview_720p` varchar(255) DEFAULT NULL,
  `content_preview_480p` varchar(255) DEFAULT NULL,
  `content_preview_360p` varchar(255) DEFAULT NULL,
  `content_preview_240p` varchar(255) DEFAULT NULL,
  `content_preview_144p` varchar(255) DEFAULT NULL,
  `thumbnail_1080p` varchar(255) DEFAULT NULL,
  `thumbnail_720p` varchar(255) DEFAULT NULL,
  `thumbnail_480p` varchar(255) DEFAULT NULL,
  `thumbnail_360p` varchar(255) DEFAULT NULL,
  `thumbnail_240p` varchar(255) DEFAULT NULL,
  `thumbnail_144p` varchar(255) DEFAULT NULL,
  `social_media_video` varchar(255) DEFAULT NULL,
  `video_length_seconds` varchar(45) DEFAULT NULL,
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  PRIMARY KEY (`content_id`),
  UNIQUE KEY `content_id_UNIQUE` (`content_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `content_title_idx` (`content_title`),
  KEY `FK_user_content_content_type_idx` (`content_type`),
  KEY `FK_content_category_id_idx` (`category_id`),
  KEY `FK_content_parent_idx` (`parent`),
  CONSTRAINT `FK_content_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_content_content_type` FOREIGN KEY (`content_type`) REFERENCES `content_type` (`content_type_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_content_parent` FOREIGN KEY (`parent`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_content_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=14208 DEFAULT CHARSET=utf8;CREATE TABLE `content_curation` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=14247 DEFAULT CHARSET=utf8;CREATE TABLE `content_curation` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `content_type` int(2) DEFAULT '0',
  `content_url` varchar(255) DEFAULT NULL,
  `content_title` varchar(45) DEFAULT NULL,
  `content_description` varchar(100) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT 'http://telecoms.com/wp-content/blogs.dir/1/files/2012/06/euro-football-sport.jpg',
  `content_likes` int(10) DEFAULT '0',
  `content_views` bigint(15) DEFAULT '0',
  `content_displays` bigint(15) DEFAULT '0',
  `content_comments` int(10) DEFAULT '0',
  `category_id` int(2) DEFAULT '1',
  PRIMARY KEY (`content_id`),
  UNIQUE KEY `content_id_UNIQUE` (`content_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `content_curation_title_idx` (`content_title`),
  KEY `FK_user_content_curation_content_type_idx` (`content_type`),
  KEY `FK_content_curation_category_id_idx` (`category_id`),
  CONSTRAINT `FK_content_curation_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_user_content_curation_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `newsfeed` (
  `user_id` int(11) NOT NULL,
  `newsfeed_object` longtext NOT NULL,
  `last_generated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  CONSTRAINT `FK_newsfeed_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `content_saved` (
  `content_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  KEY `fk_lists_workout_content_id_idx` (`content_id`),
  KEY `fk_lists_workout_user_id_idx` (`user_id`),
  CONSTRAINT `fk_content_saved_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_lists_saved_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `content_likes` (
  `user_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  `like_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`content_id`),
  KEY `FK_content_likes_content_id_idx` (`content_id`),
  CONSTRAINT `FK_content_likes_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_content_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `content_views` (
  `content_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `view_datetime` datetime DEFAULT NULL,
<<<<<<< HEAD
  PRIMARY KEY (`content_id`,`user_id`),
  KEY `FK_content_views_user_id_idx` (`user_id`),
  CONSTRAINT `FK_content_views_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_content_views_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
=======
  KEY `FK_content_views_content_id_idx` (`content_id`),
  KEY `FK_content_views_user_id_idx` (`user_id`),
  CONSTRAINT `FK_content_views_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_content_views_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_message` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `feedback_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`feedback_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;CREATE TABLE `reporting` (
  `reporting_id` int(11) NOT NULL AUTO_INCREMENT,
  `submitter_user_id` int(11) DEFAULT NULL,
  `reported_user_id` int(11) DEFAULT NULL,
  `reporting_type_id` int(11) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `reporting_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`reporting_id`),
  KEY `fk_reporting_user_id_idx` (`reported_user_id`),
  KEY `fk_reporting_submitter_id_idx` (`submitter_user_id`),
  CONSTRAINT `FK_user_reporting_user_id` FOREIGN KEY (`reported_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;CREATE TABLE `notification` (
=======
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `notification` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `notification_text` varchar(80) DEFAULT NULL,
  `notification_action` varchar(45) DEFAULT NULL,
  `notification_badge` int(11) DEFAULT '0',
  `notification_sound` int(11) DEFAULT '0',
  `notification_status` int(11) DEFAULT '0',
  `notification_image` varchar(255) NOT NULL DEFAULT 'http://peakapp.me/img/app_assets/avatar.png',
  `notification_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  KEY `FK_notification_user_id_idx` (`user_id`),
  CONSTRAINT `FK_notification_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=1802 DEFAULT CHARSET=utf8;CREATE TABLE `order` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=2062 DEFAULT CHARSET=utf8;CREATE TABLE `order` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_uuid` varchar(255) DEFAULT NULL,
  `order_type` int(11) DEFAULT NULL,
  `order_status` int(11) DEFAULT NULL,
  `order_origin` int(1) DEFAULT NULL,
  `publisher_id` int(11) DEFAULT NULL,
  `buyer_id` int(11) DEFAULT NULL,
  `content_id` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `net_revenue` int(11) DEFAULT NULL,
  `currency_id` int(11) DEFAULT NULL,
  `publisher_share` decimal(10,2) DEFAULT NULL,
  `peak_share` decimal(10,2) DEFAULT NULL,
  `publisher_balance` decimal(10,2) DEFAULT NULL,
  `peak_balance` decimal(10,2) DEFAULT NULL,
  `receipt_html` varchar(512) DEFAULT NULL,
  `email_sent` int(1) DEFAULT NULL,
  `buyer_details` varchar(512) DEFAULT NULL,
  `delivered` int(1) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_uuid_UNIQUE` (`order_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `user_following` (
  `user_id` int(11) NOT NULL,
  `following_id` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  KEY `followed_id` (`following_id`),
  KEY `user_id` (`user_id`),
<<<<<<< HEAD
  CONSTRAINT `FK_user_following_following_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_following_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
=======
  CONSTRAINT `FK_user_following_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_following_following_id` FOREIGN KEY (`following_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `category_following` (
  `category_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  KEY `FK_category_following_category_id_idx` (`category_id`),
  KEY `FK_category_following_user_id_idx` (`user_id`),
  CONSTRAINT `FK_category_following_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_category_following_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `category_publishing` (
  `category_publishing_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`category_publishing_id`),
  KEY `FK_category_publishing_category_id_idx` (`category_id`),
  KEY `FK_category_publishing_user_id_idx` (`user_id`),
  CONSTRAINT `FK_category_publishing_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_category_publishing_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;CREATE TABLE `device` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;CREATE TABLE `device` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `device_uuid` varchar(255) NOT NULL DEFAULT 'unknown',
  `device_name` varchar(255) DEFAULT NULL,
  `device_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`device_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `authentication` (
  `authentication_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `key` varchar(45) DEFAULT NULL,
  `device_uuid` varchar(255) NOT NULL DEFAULT 'unknown',
  `ip_address` varchar(45) DEFAULT NULL,
  `mac_address` varchar(45) DEFAULT NULL,
  `geolocation` varchar(45) DEFAULT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `expires` timestamp NULL DEFAULT NULL,
  `last_activity` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`),
  KEY `FK_authentication_user_id` (`user_id`),
  KEY `FK_authentication_device_uuid_idx` (`device_uuid`),
  CONSTRAINT `FK_authentication_device_uuid` FOREIGN KEY (`device_uuid`) REFERENCES `device` (`device_uuid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_authentication_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=30819 DEFAULT CHARSET=utf8;CREATE TABLE `bundle_match` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=30890 DEFAULT CHARSET=utf8;CREATE TABLE `bundle_match` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `bundle_match_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_content_id` int(11) DEFAULT '0',
  `child_content_id` int(11) DEFAULT '0',
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`bundle_match_id`),
  KEY `FK_content_content_id_bundle_match_parent_content_id_idx` (`parent_content_id`),
  KEY `FK_content_content_id_bundle_match_child_content_id` (`child_content_id`),
  CONSTRAINT `FK_content_content_id_bundle_match_child_content_id` FOREIGN KEY (`child_content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_content_content_id_bundle_match_parent_content_id` FOREIGN KEY (`parent_content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;CREATE TABLE `content_comments` (
=======
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;CREATE TABLE `content_comments` (
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  `comment_value` varchar(255) DEFAULT NULL,
  `comment_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_id_UNIQUE` (`comment_id`),
  KEY `fk_content_comments_user_id_idx` (`user_id`),
  CONSTRAINT `fk_content_comments_usr_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;CREATE TABLE `content_access` (
  `content_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
=======
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;CREATE TABLE `content_access` (
  `content_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`content_id`),
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  KEY `fk_content_access_content_id_idx` (`content_id`),
  KEY `fk_content_access_user_id_idx` (`user_id`),
  CONSTRAINT `fk_content_access_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_content_access_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `fb_user` (
  `user_id` int(11) NOT NULL,
<<<<<<< HEAD
  `link_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idfb_users_UNIQUE` (`user_id`),
=======
  `fb_user_id` varchar(50) DEFAULT NULL,
  `link_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idfb_users_UNIQUE` (`user_id`),
  UNIQUE KEY `fb_user_id_UNIQUE` (`fb_user_id`),
>>>>>>> c2d23ac3827bfdc46b5ad4e884f365952c265419
  CONSTRAINT `fk_fb_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `reporting_type` (
  `reporting_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `reporting_type_name` varchar(255) NOT NULL,
  PRIMARY KEY (`reporting_type_id`),
  UNIQUE KEY `reporting_type_name` (`reporting_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;CREATE TABLE `order_origin` (
  `order_origin_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_origin_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`order_origin_id`),
  UNIQUE KEY `order_origin_name_UNIQUE` (`order_origin_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;CREATE TABLE `order_status` (
  `order_uuid` varchar(255) NOT NULL,
  `order_status_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`order_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;CREATE TABLE `order_type` (
  `order_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_type_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`order_type_id`),
  UNIQUE KEY `order_type_name_UNIQUE` (`order_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

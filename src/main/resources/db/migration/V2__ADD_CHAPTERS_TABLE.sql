CREATE TABLE `chapter` (
  `id` int(11) NOT NULL,
  `bookId` int(11) DEFAULT NULL COMMENT 'foreign key to books',
  `chapterName` text NOT NULL,
  `chapterNumber` text NOT NULL,
  `text` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
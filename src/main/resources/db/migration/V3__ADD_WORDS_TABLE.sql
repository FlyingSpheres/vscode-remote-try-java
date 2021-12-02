CREATE TABLE `words` (
  `id` int(11) NOT NULL,
  `word` text NOT NULL,
  `translation` text DEFAULT NULL,
  `knownStatusId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
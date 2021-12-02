CREATE TABLE `phrases` (
  `id` int(11) NOT NULL,
  `phrase` text NOT NULL,
  `translation` text NOT NULL,
  `knownStatusId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
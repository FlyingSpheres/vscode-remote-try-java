CREATE TABLE `chapterPhrasesXref` (
  `id` int(11) NOT NULL,
  `chapterId` int(11) NOT NULL COMMENT 'foreign key to chapter ',
  `phraseId` int(11) NOT NULL COMMENT 'foriuegn key to phrases'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
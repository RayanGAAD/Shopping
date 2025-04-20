-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 20 avr. 2025 à 16:44
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `shopping2025`
--

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) DEFAULT NULL,
  `description` text,
  `marque` varchar(50) DEFAULT NULL,
  `prixUnitaire` double DEFAULT NULL,
  `prixGros` double DEFAULT NULL,
  `quantiteEnStock` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id`, `nom`, `description`, `marque`, `prixUnitaire`, `prixGros`, `quantiteEnStock`) VALUES
(1, 'Briquet', NULL, 'Bic', 0.5, 4, 9),
(2, 'Stylo', NULL, 'Pilot', 1.2, 5, 0),
(3, 'Cahier', NULL, 'Oxford', 3, 12, 0);

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mot_de_passe` varchar(100) DEFAULT NULL,
  `type` enum('ancien','nouveau','client','admin') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`, `email`, `mot_de_passe`, `type`) VALUES
(1, 'Jean Dupont', 'jean@email.com', 'mdp123', 'ancien'),
(2, 'Marie Curie', 'marie@email.com', 'pass456', 'nouveau'),
(3, 'Alice Dupont', 'alice.dupont@example.com', 'secret123', 'client'),
(4, 'Alice Dupont', 'alice@example.com', 'secret123', 'client'),
(5, 'Chloe Lestic', 'cloclo@example.com', 'secret123', 'client'),
(6, 'Alice Dupont', 'rayan@example.com', 'secret123', 'client'),
(7, 'Alice Dupont', 'wassim@example.com', 'secret123', 'client'),
(8, 'Alice Dupont', 'caca@example.com', 'secret123', 'client'),
(9, 'gaad', 'Rayan', 'vdfsvf', 'nouveau'),
(10, 'GAAD', 'bonjour@gmail.com', 'fezadaz', 'client'),
(11, 'fsegrg', 'sdfsef@sgesf.fr', 'oihgnrt', 'ancien'),
(12, 'gdfg', 'gdfg', 'gdgesf', 'ancien'),
(13, 'gdfgze', 'gdfghfgh', 'gdgesf', 'ancien'),
(14, 'hjyg', 'rjyty', 'okiui', 'ancien'),
(15, 'uyuk', 'lou', 't', 'ancien'),
(16, 'o', 'u', 'y', 'nouveau'),
(17, 'hd', 'loi', 'z', 'ancien'),
(18, 'rth', 'fjty', 'rtr', 'ancien'),
(19, 'tdr', 'dyf', 'drt', 'ancien'),
(20, 'getg', 'rteg', 'eg', 'ancien'),
(21, 'gbf', 'gr', 'z', 'ancien'),
(22, 'ut', 'urt', 'te', 'ancien'),
(23, 'd', 'bg', 'gb', 'ancien'),
(24, 'ret', 'eyerty', '\'(yetr', 'ancien'),
(39, 'cvb', 'bfb', 'dfg', 'ancien'),
(26, 'gdf', 'dhdg', 'se', 'ancien'),
(27, 'gd', 'gdtrg', 'r', 'ancien'),
(28, 'ge', 'tdr', 'tre', 'ancien'),
(29, 'ef', 'ezz', 'z', 'ancien'),
(30, 'd', 'z', 'a', 'ancien'),
(31, '\"', 'e', 'a', 'ancien'),
(32, 'f', 'etjyfy', 'a', 'ancien'),
(33, 'htfhte', 'fdg', ',hhhhgf', 'ancien'),
(34, 'nb', 'dffgb', 'dfngdfg', 'ancien'),
(35, 'te', 'drtu', 'zerzs', 'ancien'),
(36, 'dhfghd', 'fghd', 'hdrfhdt', 'ancien'),
(37, 'fgfd', 'hdj', 'gholui', 'ancien'),
(38, 'gdfg', 'shdrh', 'tdyhdr', 'ancien'),
(40, 'hd', 'fhtrt', 'thht', 'ancien'),
(41, 'yt', 'drty', 'ty', 'ancien'),
(42, 'gserg', 'htdrgh', 'rhtdrtg', 'ancien'),
(43, 'gdh', 'dhth', 'z', 'ancien'),
(44, 'fve', 'ffzref\"', '\"\'rf\"\'rfetgbet', 'ancien'),
(45, 'zegrzf', 'gergzrg', 'zrgzr\'frger', 'ancien'),
(46, 'htrheh', 'erherth', 'herth', 'ancien'),
(47, 'ege', 'rge', 'rgerg', 'ancien'),
(48, 'gr', 'g', 'gr', 'ancien');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `id` int NOT NULL AUTO_INCREMENT,
  `client_id` int DEFAULT NULL,
  `date_commande` datetime NOT NULL,
  `montant_total` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`id`, `client_id`, `date_commande`, `montant_total`) VALUES
(1, 0, '2025-04-18 15:44:16', 0.5),
(2, 0, '2025-04-18 15:49:27', 3),
(3, 0, '2025-04-18 15:52:29', 3),
(4, 0, '2025-04-18 15:55:16', 0.5),
(5, 0, '2025-04-18 16:32:31', 0.5),
(6, 0, '2025-04-18 16:32:54', 2),
(7, 1, '2025-04-18 17:07:05', 0.5),
(8, 1, '2025-04-18 17:07:37', 2.5),
(9, 1, '2025-04-18 17:12:54', 0.5),
(10, 1, '2025-04-18 17:31:30', 1),
(11, 1, '2025-04-19 17:37:04', 1.2),
(12, 1, '2025-04-19 19:45:36', 3),
(13, 1, '2025-04-19 19:59:15', 1.2),
(14, 1, '2025-04-19 20:15:10', 0.5),
(15, 1, '2025-04-19 20:32:24', 6),
(16, 1, '2025-04-19 20:55:03', 1.2),
(17, 1, '2025-04-19 21:05:38', 3),
(18, 45, '2025-04-19 22:17:41', 1.2),
(19, 1, '2025-04-20 14:46:16', 3),
(20, 46, '2025-04-20 17:56:34', 1.2),
(21, 48, '2025-04-20 18:28:04', 0.5);

-- --------------------------------------------------------

--
-- Structure de la table `commande_article`
--

DROP TABLE IF EXISTS `commande_article`;
CREATE TABLE IF NOT EXISTS `commande_article` (
  `commande_id` int NOT NULL,
  `article_id` int NOT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`commande_id`,`article_id`),
  KEY `article_id` (`article_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande_article`
--

INSERT INTO `commande_article` (`commande_id`, `article_id`, `quantite`) VALUES
(3, 3, 1),
(4, 1, 1),
(5, 1, 1),
(6, 1, 4),
(7, 1, 1),
(8, 1, 5),
(9, 1, 1),
(10, 1, 2),
(11, 2, 1),
(12, 3, 1),
(13, 2, 1),
(14, 1, 1),
(15, 3, 2),
(16, 2, 1),
(17, 3, 1),
(18, 2, 1),
(19, 3, 1),
(20, 2, 1),
(21, 1, 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

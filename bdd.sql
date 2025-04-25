-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 18 avr. 2025 à 12:43
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
                                                                                                                (1, 'Briquet', NULL, 'Bic', 0.5, 4, 10),
                                                                                                                (2, 'Stylo', NULL, 'Pilot', 1.2, 5, 5),
                                                                                                                (3, 'Cahier', NULL, 'Oxford', 3, 12, 5);

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
    ) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
(25, 'vdf', 'dfbd', 'k', 'ancien'),
(26, 'gdf', 'dhdg', 'se', 'ancien'),
(27, 'gd', 'gdtrg', 'r', 'ancien');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `id` int NOT NULL AUTO_INCREMENT,
  `client_id` int DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
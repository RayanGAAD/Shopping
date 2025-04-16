-- Création de la base de données (si elle n'existe pas déjà)
CREATE DATABASE IF NOT EXISTS shopping2025;
USE shopping2025;

-- Table Client
DROP TABLE IF EXISTS client;
CREATE TABLE client (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nom VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        mot_de_passe VARCHAR(255) NOT NULL,
                        type ENUM('ancien', 'nouveau', 'client', 'admin') NOT NULL
) ENGINE=InnoDB;

-- Table Article
DROP TABLE IF EXISTS article;
CREATE TABLE article (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         description TEXT,
                         prixUnitaire DOUBLE NOT NULL,
                         prixGros DOUBLE,
                         quantiteEnStock INT NOT NULL,
                         Marque VARCHAR(50)  -- Stocke le nom de la marque (exemple : "Bic")
) ENGINE=InnoDB;

-- Table Commande
DROP TABLE IF EXISTS commande;
CREATE TABLE commande (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          idClient INT NOT NULL,
                          dateCommande DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          montantTotal DOUBLE NOT NULL,
                          FOREIGN KEY (idClient) REFERENCES client(id)
) ENGINE=InnoDB;

-- Table LigneCommande
DROP TABLE IF EXISTS ligneCommande;
CREATE TABLE ligneCommande (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               idCommande INT NOT NULL,
                               idArticle INT NOT NULL,
                               quantite INT NOT NULL,
                               FOREIGN KEY (idCommande) REFERENCES commande(id),
                               FOREIGN KEY (idArticle) REFERENCES article(id)
) ENGINE=InnoDB;

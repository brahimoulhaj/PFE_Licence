-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 29, 2018 at 05:12 PM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sihaty`
--
CREATE DATABASE Sihaty;
-- --------------------------------------------------------

--
-- Table structure for table `dossiers`
--

CREATE TABLE `dossiers` (
  `id` int(11) NOT NULL,
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  `specialite` varchar(100),
  `patient_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `medecins`
--

CREATE TABLE `medecins` (
  `id` int(11) NOT NULL,
  `nom_prenom` varchar(100) NOT NULL,
  `specialite` varchar(100) NOT NULL,
  `telephone` varchar(10) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `login` varchar(30) NOT NULL,
  `pwd` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ordonnances`
--

CREATE TABLE `ordonnances` (
  `id` int(11) NOT NULL,
  `file` varchar(50) NOT NULL,
  `dossier_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `ordonnances`
--

CREATE TABLE `analyses` (
  `id` int(11) NOT NULL,
  `file` varchar(50) NOT NULL,
  `dossier_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `nom_prenom` varchar(100) NOT NULL,
  `cin` varchar(10) NOT NULL,
  `date_naissance` date NOT NULL,
  `telephone` varchar(10) NOT NULL,
  `sexe` char(1) NOT NULL,
  `adresse` varchar(250) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `photo` varchar(60) DEFAULT NULL,
  `date_inscription` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isConfirmed` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `patient_notif`
--

CREATE TABLE `patient_notif` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `text` text,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `patient_ID` int(11) DEFAULT NULL,
  `seen` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `rendezvous`
--

CREATE TABLE `rendezvous` (
  `id` int(11) NOT NULL,
  `patient_ID` int(11) NOT NULL,
  `specialite` varchar(100) DEFAULT NULL,
  `jour` date NOT NULL,
  `heure` time NOT NULL DEFAULT '08:00:00',
  `isConfirmed` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `specialites`
--

CREATE TABLE `specialites` (
  `specialite` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------


CREATE VIEW `touslesrdv`  AS  
	select `rdv`.`id` AS `id`,`rdv`.`patient_ID` AS `patient_ID`, `rdv`.`specialite` AS `specialite`,`rdv`.`jour` AS `jour`,`rdv`.`heure` AS `heure`,`rdv`.`isConfirmed` AS `isConfirmed`,`pat`.`nom_prenom` AS `nom_prenom`,`pat`.`cin` AS `cin`
 from (`rendezvous` `rdv` join `patients` `pat`)
 where (`rdv`.`patient_ID` = `pat`.`id`)
 order by `rdv`.`isConfirmed`,`rdv`.`jour` desc,`rdv`.`heure` desc,`pat`.`nom_prenom`, `pat`.`id` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dossiers`
--
ALTER TABLE `dossiers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_patients_dossiers` (`patient_id`);

--
-- Indexes for table `medecins`
--
ALTER TABLE `medecins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `telephone` (`telephone`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `specialite` (`specialite`);

--
-- Indexes for table `ordonnances`
--
ALTER TABLE `ordonnances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dossier_id` (`dossier_id`);
  
  --
-- Indexes for table `analyses`
--
ALTER TABLE `analyses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dossier_id` (`dossier_id`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cin` (`cin`),
  ADD UNIQUE KEY `telephone` (`telephone`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `patient_notif`
--
ALTER TABLE `patient_notif`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pat_id` (`patient_ID`);

--
-- Indexes for table `rendezvous`
--
ALTER TABLE `rendezvous`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patient_ID` (`patient_ID`);

--
-- Indexes for table `specialites`
--
ALTER TABLE `specialites`
  ADD PRIMARY KEY (`specialite`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dossiers`
--
ALTER TABLE `dossiers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `medecins`
--
ALTER TABLE `medecins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ordonnances`
--
ALTER TABLE `ordonnances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
  
  --
-- AUTO_INCREMENT for table `analyses`
--
ALTER TABLE `analyses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `patient_notif`
--
ALTER TABLE `patient_notif`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rendezvous`
--
ALTER TABLE `rendezvous`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dossiers`
--
ALTER TABLE `dossiers`
  ADD CONSTRAINT `fk_specialite_dossiers` FOREIGN KEY (`specialite`) REFERENCES `specialites` (`specialite`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_patients_dossiers` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `medecins`
--
ALTER TABLE `medecins`
  ADD CONSTRAINT `medecins_ibfk_1` FOREIGN KEY (`specialite`) REFERENCES `specialites` (`specialite`) ON DELETE CASCADE;

--
-- Constraints for table `ordonnances`
--
ALTER TABLE `ordonnances`
  ADD CONSTRAINT `ordonnances_ibfk_2` FOREIGN KEY (`dossier_id`) REFERENCES `dossiers` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `patient_notif`
--
ALTER TABLE `patient_notif`
  ADD CONSTRAINT `fk_pat_id` FOREIGN KEY (`patient_ID`) REFERENCES `patients` (`id`) ON DELETE CASCADE;
  

--
-- Constraints for table `rendezvous`
--
ALTER TABLE `rendezvous`
  ADD CONSTRAINT `fk_specialite_rdv` FOREIGN KEY (`specialite`) REFERENCES `specialites` (`specialite`) ON DELETE CASCADE,
  ADD CONSTRAINT `rendezvous_ibfk_1` FOREIGN KEY (`patient_ID`) REFERENCES `patients` (`id`) ON DELETE CASCADE;
  
  --
-- Constraints for table `analyses`
--
ALTER TABLE `analyses`
  ADD CONSTRAINT `analyses_ibfk_2` FOREIGN KEY (`dossier_id`) REFERENCES `dossiers` (`id`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

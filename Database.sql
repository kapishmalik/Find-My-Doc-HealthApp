-- phpMyAdmin SQL Dump
-- version 3.5.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 26, 2016 at 01:01 PM
-- Server version: 5.5.32
-- PHP Version: 5.4.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `1046389`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE IF NOT EXISTS `appointments` (
  `patientemail` varchar(100) DEFAULT NULL,
  `docemail` varchar(100) DEFAULT NULL,
  `appointmentid` int(11) NOT NULL AUTO_INCREMENT,
  `clinicid` int(11) DEFAULT NULL,
  `appointmentdate` date DEFAULT NULL,
  `slotid` int(11) DEFAULT NULL,
  `appointmenttime` time DEFAULT NULL,
  `appointmentstatuscode` int(11) DEFAULT NULL,
  `purpose` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`appointmentid`),
  KEY `docemail` (`docemail`),
  KEY `patientemail` (`patientemail`),
  KEY `clinicid` (`clinicid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=37 ;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`patientemail`, `docemail`, `appointmentid`, `clinicid`, `appointmentdate`, `slotid`, `appointmenttime`, `appointmentstatuscode`, `purpose`) VALUES
('malikkapish@gmail.com', 'meetika222@gmail.com', 26, 3, '2015-12-01', 31, '10:00:00', 2, 'Viral'),
('avnimalhan20@gmail.com', 'meetika222@gmail.com', 27, 3, '2015-12-02', 31, '11:15:00', 0, 'not well'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 24, 1, '2015-12-15', 28, '10:00:00', 2, 'Fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 32, 1, '2015-12-15', 28, '09:00:00', 1, 'fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 31, 1, '2015-12-15', 28, '09:15:00', 1, 'tatti nahi hoti'),
('meetika222@gmail.com', 'meetika222@gmail.com', 30, 1, '2015-12-15', 28, '09:00:00', 1, 'headache'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 29, 1, '2015-12-15', 28, '09:00:00', 1, 'fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 28, 1, '2015-12-04', 28, '09:00:00', 1, 'fever '),
('malikkapish@gmail.com', 'meetika222@gmail.com', 23, 1, '2015-12-01', 28, '09:00:00', 1, 'fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 22, 1, '2015-11-29', 28, '09:00:00', 3, 'Fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 34, 1, '2015-12-18', 28, '11:30:00', 2, 'fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 35, 1, '2015-12-27', 28, '09:45:00', 0, 'fever'),
('malikkapish@gmail.com', 'meetika222@gmail.com', 36, 1, '2016-01-07', 28, '09:00:00', 2, 'fever');

-- --------------------------------------------------------

--
-- Table structure for table `clinicdetails`
--

CREATE TABLE IF NOT EXISTS `clinicdetails` (
  `clinicid` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `contactno` varchar(10) DEFAULT NULL,
  `fees` int(11) DEFAULT NULL,
  PRIMARY KEY (`clinicid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `clinicdetails`
--

INSERT INTO `clinicdetails` (`clinicid`, `name`, `address`, `contactno`, `fees`) VALUES
(1, 'Batra', 'A-1, Paschim Vihar  New Delhi India   ', '9874123695', 500),
(2, 'Irene Hospital', 'Harkesh Nagar, Okhla Industrial Area   Okhla Industrial Area  New Delhi  Delhi  India   ', '895642399', 200),
(3, 'Mediclinic', 'lajpat nagar', '9810258369', 200);

-- --------------------------------------------------------

--
-- Table structure for table `clinicrdoc`
--

CREATE TABLE IF NOT EXISTS `clinicrdoc` (
  `clinicid` int(11) NOT NULL DEFAULT '0',
  `docemail` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`clinicid`,`docemail`),
  KEY `docemail` (`docemail`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `clinicrdoc`
--

INSERT INTO `clinicrdoc` (`clinicid`, `docemail`) VALUES
(1, 'meetika222@gmail.com'),
(2, 'karishma15027@iiitd.ac.in'),
(3, 'meetika222@gmail.com'),
(4, 'avni15012@iiitd.ac.in');

-- --------------------------------------------------------

--
-- Table structure for table `doctor`
--

CREATE TABLE IF NOT EXISTS `doctor` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `yrsofexp` int(11) DEFAULT NULL,
  `medicalcouncilno` varchar(50) DEFAULT NULL,
  `qualificationid` int(11) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`email`),
  KEY `qualificationid` (`qualificationid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`email`, `name`, `gender`, `age`, `yrsofexp`, `medicalcouncilno`, `qualificationid`, `password`) VALUES
('avni15012@iiitd.ac.in', 'Avni Malhan', 'Female', 0, 0, '37', 3, '37a6259cc0c1dae299a7866489dff0bd'),
('meetika222@gmail.com', 'Meetika Anand', 'Female', 23, 2, '1110', 1, 'dfefe38ff0dbad60f2b096f9da61d597'),
('karishma15027@iiitd.ac.in', 'KARISHMA TIRTHANI', 'Female', 0, 0, '1234', 2, '37a6259cc0c1dae299a7866489dff0bd');

-- --------------------------------------------------------

--
-- Table structure for table `patient`
--

CREATE TABLE IF NOT EXISTS `patient` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `contactno` varchar(11) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `address` varchar(150) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patient`
--

INSERT INTO `patient` (`email`, `name`, `contactno`, `password`, `address`, `age`, `gender`) VALUES
('deepali12033@iiitd.ac.in', 'Deepali Kishnani', '', NULL, '', 0, 'Female'),
('meetika222@gmail.com', 'Meetika', '9874267138', 'dfefe38ff0dbad60f2b096f9da61d597', 'paschim vihar', 13, 'Female'),
('kapish15026@iiitd.ac.in', 'KAPISH MALIK', NULL, NULL, NULL, 0, 'Male'),
('avnimalhan20@gmail.com', 'avni malhan', '9958263926', '5d9d9f8b2f0d3e7513cac85a8b52c3d4', 'noida', 22, 'Female'),
('karishma15027@iiitd.ac.in', 'KARISHMA TIRTHANI', NULL, NULL, NULL, 0, 'Female'),
('neetiarora92@gmail.com', 'Neeti Arora', '9711498211', 'c41fbb610892f6947a702b4ddd6eaeb0', 'Lajpat Nagar', 21, 'Female'),
('karishma.tirthani08@gmail.com', 'Karishma Tirthani', NULL, NULL, NULL, 0, 'Female'),
('malikkapish@gmail.com', 'Kapish Malik', '9711498211', 'c41fbb610892f6947a702b4ddd6eaeb0', 'Tagore Garden Extension ', 24, 'Male');

-- --------------------------------------------------------

--
-- Table structure for table `qualificationdetails`
--

CREATE TABLE IF NOT EXISTS `qualificationdetails` (
  `qualificationid` int(11) NOT NULL,
  `ugdegree` varchar(50) DEFAULT NULL,
  `pgdegree` varchar(50) DEFAULT NULL,
  `otherdegree` varchar(50) DEFAULT NULL,
  `speciality` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`qualificationid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `qualificationdetails`
--

INSERT INTO `qualificationdetails` (`qualificationid`, `ugdegree`, `pgdegree`, `otherdegree`, `speciality`) VALUES
(4, 'B.D.S', 'M.D.S', '', 'Dentist'),
(3, 'B.D.S', 'M.D.S', '', 'Dentist'),
(1, 'M.B.B.S', 'M.D', '', 'Cardiology'),
(2, 'M.B.B.S', 'M.D', 'na', 'General Physician');

-- --------------------------------------------------------

--
-- Table structure for table `slots`
--

CREATE TABLE IF NOT EXISTS `slots` (
  `slotid` int(11) NOT NULL AUTO_INCREMENT,
  `clinicid` int(11) DEFAULT NULL,
  `starttime` time DEFAULT NULL,
  `endtime` time DEFAULT NULL,
  PRIMARY KEY (`slotid`),
  KEY `clinicid` (`clinicid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=34 ;

--
-- Dumping data for table `slots`
--

INSERT INTO `slots` (`slotid`, `clinicid`, `starttime`, `endtime`) VALUES
(33, 3, '19:00:00', '21:00:00'),
(32, 1, '17:00:00', '19:00:00'),
(31, 3, '10:00:00', '12:15:00'),
(30, 2, '17:00:00', '18:05:00'),
(28, 1, '09:00:00', '12:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `workingdates`
--

CREATE TABLE IF NOT EXISTS `workingdates` (
  `clinicid` int(11) NOT NULL DEFAULT '0',
  `dates` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`clinicid`,`dates`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `workingdates`
--

INSERT INTO `workingdates` (`clinicid`, `dates`) VALUES
(1, '2015-11-04'),
(1, '2015-11-29'),
(1, '2015-12-01'),
(1, '2015-12-02'),
(1, '2015-12-03'),
(1, '2015-12-04'),
(1, '2015-12-15'),
(1, '2015-12-17'),
(1, '2015-12-18'),
(1, '2015-12-21'),
(1, '2015-12-27'),
(1, '2015-12-28'),
(1, '2016-01-07'),
(1, '2016-01-08'),
(1, '2016-01-09'),
(2, '2015-11-28'),
(2, '2015-12-06'),
(3, '2015-11-02'),
(3, '2015-12-01'),
(3, '2015-12-02');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

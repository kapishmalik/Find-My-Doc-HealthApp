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


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

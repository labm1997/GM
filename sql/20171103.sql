-- MySQL dump 10.16  Distrib 10.1.26-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: db_tp1
-- ------------------------------------------------------
-- Server version	10.1.26-MariaDB-0+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_cursos`
--

DROP TABLE IF EXISTS `tbl_cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_cursos` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) DEFAULT NULL,
  `totalcreditos` int(3) DEFAULT NULL,
  `minimocreditos` int(3) DEFAULT '10',
  `maximocreditos` int(3) DEFAULT '34',
  `ID_COORDENADOR` varchar(10) DEFAULT NULL,
  `modalidade` varchar(10) DEFAULT 'Presencial',
  `turno` varchar(10) DEFAULT 'Diurno',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_cursos`
--

LOCK TABLES `tbl_cursos` WRITE;
/*!40000 ALTER TABLE `tbl_cursos` DISABLE KEYS */;
INSERT INTO `tbl_cursos` VALUES (1,'Engenharia de Computação',252,18,32,'10/2878456','Presencial','Diurno');
/*!40000 ALTER TABLE `tbl_cursos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_cursos_materias`
--

DROP TABLE IF EXISTS `tbl_cursos_materias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_cursos_materias` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `type` varchar(3) DEFAULT 'OBR',
  `ID_CURSO` int(4) DEFAULT NULL,
  `ID_MATERIA` int(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_cursos_materias`
--

LOCK TABLES `tbl_cursos_materias` WRITE;
/*!40000 ALTER TABLE `tbl_cursos_materias` DISABLE KEYS */;
INSERT INTO `tbl_cursos_materias` VALUES (1,'OBR',1,1);
/*!40000 ALTER TABLE `tbl_cursos_materias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_departamentos`
--

DROP TABLE IF EXISTS `tbl_departamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_departamentos` (
  `ID` int(3) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) DEFAULT NULL,
  `sigla` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_departamentos`
--

LOCK TABLES `tbl_departamentos` WRITE;
/*!40000 ALTER TABLE `tbl_departamentos` DISABLE KEYS */;
INSERT INTO `tbl_departamentos` VALUES (1,'Departamento de Matemática','MAT'),(2,'Instituto de Física','IFD');
/*!40000 ALTER TABLE `tbl_departamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_materias`
--

DROP TABLE IF EXISTS `tbl_materias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_materias` (
  `ID` int(6) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) DEFAULT NULL,
  `ID_DEPARTAMENTO` int(3) DEFAULT NULL,
  `ementa` text,
  `creditos` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_materias`
--

LOCK TABLES `tbl_materias` WRITE;
/*!40000 ALTER TABLE `tbl_materias` DISABLE KEYS */;
INSERT INTO `tbl_materias` VALUES (1,'Introdução à Álgebra Linear',1,'Nessa matéria aprende-se a ser matemacho.',4),(2,'Relatividade e Física Quântica',2,'Nessa matéria aprende-se a ser físico.',6);
/*!40000 ALTER TABLE `tbl_materias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_turmas`
--

DROP TABLE IF EXISTS `tbl_turmas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_turmas` (
  `ID` int(6) NOT NULL AUTO_INCREMENT,
  `letra` varchar(2) DEFAULT NULL,
  `professor` varchar(10) DEFAULT NULL,
  `vagas` int(3) DEFAULT NULL,
  `ID_MATERIA` int(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_turmas`
--

LOCK TABLES `tbl_turmas` WRITE;
/*!40000 ALTER TABLE `tbl_turmas` DISABLE KEYS */;
INSERT INTO `tbl_turmas` VALUES (1,'A','16/1803399',161,1);
/*!40000 ALTER TABLE `tbl_turmas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_turmas_horarios`
--

DROP TABLE IF EXISTS `tbl_turmas_horarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_turmas_horarios` (
  `ID` int(6) NOT NULL AUTO_INCREMENT,
  `dia` int(1) DEFAULT NULL,
  `inicio` datetime DEFAULT NULL,
  `fim` datetime DEFAULT NULL,
  `ID_TURMA` int(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_turmas_horarios`
--

LOCK TABLES `tbl_turmas_horarios` WRITE;
/*!40000 ALTER TABLE `tbl_turmas_horarios` DISABLE KEYS */;
INSERT INTO `tbl_turmas_horarios` VALUES (1,3,'1000-01-01 16:00:00','1000-01-01 18:00:00',1),(2,5,'1000-01-01 16:00:00','1000-01-01 18:00:00',1);
/*!40000 ALTER TABLE `tbl_turmas_horarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_turmas_matricula`
--

DROP TABLE IF EXISTS `tbl_turmas_matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_turmas_matricula` (
  `ID` int(6) NOT NULL AUTO_INCREMENT,
  `MATRICULA` varchar(10) DEFAULT NULL,
  `ID_TURMA` int(6) DEFAULT NULL,
  `status` varchar(11) DEFAULT NULL,
  `prioridade` int(1) DEFAULT NULL,
  `tipo` varchar(3) DEFAULT 'OBR',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_turmas_matricula`
--

LOCK TABLES `tbl_turmas_matricula` WRITE;
/*!40000 ALTER TABLE `tbl_turmas_matricula` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_turmas_matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_users`
--

DROP TABLE IF EXISTS `tbl_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_users` (
  `matricula` varchar(10) NOT NULL,
  `nome` varchar(40) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `semestre_inicio` varchar(6) DEFAULT NULL,
  `image` mediumblob NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `nomepai` varchar(40) DEFAULT NULL,
  `nomemae` varchar(40) DEFAULT NULL,
  `nacionalidade` varchar(30) DEFAULT NULL,
  `rg` varchar(7) DEFAULT NULL,
  `sexo` varchar(9) DEFAULT NULL,
  `curso` int(4) DEFAULT NULL,
  `data_nascimento` varchar(10) DEFAULT '00/00/0000',
  `nivel` varchar(15) DEFAULT 'Graduação',
  `bloqueado` varchar(3) DEFAULT 'Não',
  `pne` varchar(3) DEFAULT 'Não',
  `endereco` varchar(50) DEFAULT '-',
  `uf` varchar(2) DEFAULT 'DF',
  `cidade` varchar(30) DEFAULT 'Brasília',
  `cep` varchar(12) DEFAULT '-',
  `email` varchar(20) DEFAULT '-',
  `telefone` varchar(9) DEFAULT '-',
  `celular` varchar(9) DEFAULT '-',
  `racacor` varchar(10) DEFAULT '-',
  PRIMARY KEY (`matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_users`
--

LOCK TABLES `tbl_users` WRITE;
/*!40000 ALTER TABLE `tbl_users` DISABLE KEYS */;
INSERT INTO `tbl_users` VALUES ('10/2878456','Ricardo Jacobi','21232f297a57a5a743894a0e4a801fc3','coordenador','-','','12345678900','João','Maria','Italiano','1234567','Masculino',0,'00/00/0000','Graduação','Não','Não','-','DF','Brasília','-','-','-','-','-'),('16/0013615','Luiz Antônio Borges Martins','900150983cd24fb0d6963f7d28e17f72','aluno','2016/1','','06459735131','Luiz','Cáritas','Brasileiro','3496447','Masculino',1,'27/12/1997','Graduação','Não','Não','Entre dois números transcendentes','DF','Brasília','-','labm1997@gmail.com','-','-','Branca'),('16/0041465','Vinícius Campos Silva','6f94247141c8bbf580c661a034710aa5','aluno','2016/1','','04559893110','João','Alessandra','Brasileiro','3245360','Masculino',1,'00/00/0000','Graduação','Não','Não','-','DF','Brasília','-','-','-','-','-'),('16/1803399','Irina Sviridova','21232f297a57a5a743894a0e4a801fc3','professor','1900/1','','00000000000','Alexei Krasmikov','Irva Dodonov','Russa','1111111','Feminino',0,'00/00/0000','Graduação','Não','Não','-','DF','Brasília','-','-','-','-','-');
/*!40000 ALTER TABLE `tbl_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-03  6:13:00

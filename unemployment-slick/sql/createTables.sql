CREATE TABLE areas (
  areaCode CHAR(15) NOT NULL PRIMARY KEY,
  areaText VARCHAR(200) NOT NULL
);

CREATE TABLE series (
  seriesid CHAR(20) NOT NULL PRIMARY KEY,
  areaTypeCode CHAR(1) NOT NULL,
  areaCode CHAR(15) NOT NULL,
  FOREIGN KEY (areaCode) REFERENCES areas (areaCode) ON DELETE CASCADE,
  seriesTitle VARCHAR(200) NOT NULL,
  beginYear INT NOT NULL,
  beginPeriod INT NOT NULL,
  endYear INT NOT NULL,
  endPeriod INT NOT NULL
);

CREATE TABLE data (
  dataid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  seriesid CHAR(20) NOT NULL,
  FOREIGN KEY (seriesid) REFERENCES series (seriesid) ON DELETE CASCADE, 
  year INT NOT NULL,
  period INT NOT NULL,
  value DOUBLE NOT NULL
);
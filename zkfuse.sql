
DROP SCHEMA IF EXISTS `zkfuse` ;
CREATE SCHEMA IF NOT EXISTS `zkfuse` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `zkfuse` ;

DROP TABLE IF EXISTS `KeyValue` ;
DROP TABLE IF EXISTS `ResourceBundle` ;
DROP TABLE IF EXISTS `Module` ;
DROP TABLE IF EXISTS `Locale` ;

-- -----------------------------------------------------
-- Table `Locale`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Locale` (
  `LocaleId` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `LanguageCode` VARCHAR(45) NOT NULL ,
  `CountryCode` VARCHAR(45) NOT NULL DEFAULT '' ,
  `VariantCode` VARCHAR(45) NOT NULL DEFAULT '',
  `Description` VARCHAR(255) NULL,
  PRIMARY KEY (`LocaleId`),
  UNIQUE KEY `Uidx_LanguageCode_CountryCode_VariantCode` (`LanguageCode`,`CountryCode`,`VariantCode`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Module`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Module` (
  `ModuleId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(255) NOT NULL ,
  `Description` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ModuleId`) ,
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `ResourceBundle`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ResourceBundle` (
  `ResourceBundleId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ResourceBundleName` VARCHAR(255) NOT NULL ,
  `Description` VARCHAR(255) NULL ,
  `LocaleId` INT UNSIGNED NOT NULL ,
  `ModuleId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY ( `ResourceBundleId`),
  CONSTRAINT `FK_ResourceBundle_Locale_LocaleId` FOREIGN KEY ( `LocaleId` ) REFERENCES Locale ( `LocaleId` ),
  CONSTRAINT `FK_ResourceBundle_Module_ModuleId` FOREIGN KEY ( `ModuleId` ) REFERENCES Module ( `ModuleId` ),
  UNIQUE KEY `Uidx_LocaleId_ModuleId_ResourceBundleName` (`LocaleId`,`ModuleId`, `ResourceBundleName` )
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `KeyValue`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `KeyValue` (
  `KeyValueId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Property` VARCHAR(45) NOT NULL ,
  `Value` TEXT CHARACTER SET 'utf8' NOT NULL ,
  `Description` VARCHAR(255) NULL,
  `ResourceBundleId` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`KeyValueId`),
  UNIQUE KEY `Uidx_Property_ResourceBundleId` ( `Property`, `ResourceBundleId` ),
  CONSTRAINT `FK_KeyValue_ResourceBundle_ResourceBundleId` FOREIGN KEY ( `ResourceBundleId` )
	REFERENCES ResourceBundle ( `ResourceBundleId` )
)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;


-- Insert data for i18n demo
INSERT INTO Locale( `Name`, `LanguageCode`,`CountryCode`,`VariantCode`,`Description`) VALUES('Chinese', 'zh', 'CN', '', null);
INSERT INTO Locale( `Name`, `LanguageCode`,`CountryCode`,`VariantCode`,`Description`) VALUES('US','en', '', '', null);

INSERT INTO Module (Name,Description) VALUES("TestModule", "For testing");
INSERT INTO Module (Name,Description) VALUES("Payment", "Payment module");
INSERT INTO Module (Name,Description) VALUES("UserDetails", "User Details");



-- insert data for ResourceBundle with module 'TestModule' and locale = en
SET @ModuleId = (select ModuleId from Module where Name = 'TestModule');
SET @LocaleId = (select LocaleId from Locale where LanguageCode = 'en');
INSERT INTO ResourceBundle (ResourceBundleName, LocaleId, ModuleId, Description ) VALUES ( "TestModule_en", @LocaleId, @ModuleId, "Resource bundle for module=TestModule and locale=en" );

-- insert data for KeyValue for ResourceBundle with module 'TestModule' and locale = en
SET @ResourceBundleId = (select ResourceBundleId from ResourceBundle where ResourceBundleName = 'TestModule_en');
INSERT INTO KeyValue (Property, Value, Description, ResourceBundleId) VALUES( "zk", "Hello", "", @ResourceBundleId );

-- insert data for ResourceBundle with module 'TestModule' and locale = en
SET @ModuleId = (select ModuleId from Module where Name = 'UserDetails');
INSERT INTO ResourceBundle(ResourceBundleName, LocaleId, ModuleId, Description ) VALUES ( "UserDetails_en", @LocaleId, @ModuleId, "Resource bundle for module=UserDetails and locale=en" );

-- insert data for KeyValue for ResourceBundle with module 'UserDetails' and locale = en
SET @ResourceBundleId = (select ResourceBundleId from ResourceBundle where ResourceBundleName = 'UserDetails_en');
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('username','UserName','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('user.details','User Details','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('type','Type','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('content','Content','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('password','Password','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('retype.password','Re-type Password','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('age','Age','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('phone','Phone','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('weight','Weight','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('birthday','Birthday','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('address','Address','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('zip.code','Zip Code','',@ResourceBundleId);
INSERT INTO KeyValue ( Property,Value,Description,ResourceBundleId) VALUES ('email','Email','',@ResourceBundleId);


-- Tables for Shiro Security Demo

DROP TABLE IF EXISTS security_permission_role;
DROP TABLE IF EXISTS security_permission;

DROP TABLE IF EXISTS security_role_user;
DROP TABLE IF EXISTS security_role;
DROP TABLE IF EXISTS security_user;

CREATE TABLE security_permission (
    permission_id int NOT NULL AUTO_INCREMENT,
    permission_name varchar( 64 ) NOT NULL,
    description varchar( 255 ),
CONSTRAINT pk_security_permission PRIMARY KEY ( permission_id ),
CONSTRAINT idx_security_permission_unq_name UNIQUE ( permission_name )
);

CREATE TABLE security_role (
    role_id int NOT NULL AUTO_INCREMENT,
    role_name varchar( 32 ) NOT NULL,
    description varchar( 255 ),

    PRIMARY KEY ( role_id ),
    CONSTRAINT idx_security_role_unq_name UNIQUE ( role_name )
 );

 CREATE TABLE security_user (
    user_id int NOT NULL AUTO_INCREMENT,
    user_login_name varchar( 32 ) NOT NULL,
    user_password varchar( 254 ) NOT NULL,
    user_salt varchar( 254 ),

    PRIMARY KEY ( user_id ),
    CONSTRAINT idx_security_user_unq_loginname UNIQUE ( user_login_name )
);

CREATE TABLE security_permission_role (
    permission_id int NOT NULL,
    role_id int NOT NULL,
    created_date datetime NOT NULL,
    created_by varchar( 32 ) NOT NULL,

    PRIMARY KEY ( permission_id, role_id ),
    FOREIGN KEY (permission_id) REFERENCES security_permission(permission_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES security_role(role_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE security_role_user (
    role_id int NOT NULL,
    user_id int NOT NULL,
    created_date datetime NOT NULL,
    created_by varchar( 32 ) NOT NULL,

    PRIMARY KEY ( role_id, user_id ),
    FOREIGN KEY ( role_id ) REFERENCES security_role( role_id ) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ( user_id ) REFERENCES security_user( user_id ) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insert data for Shiro Security Demo

INSERT INTO security_user (user_login_name,user_password,user_salt) VALUES ('Test','aaa',null);
INSERT INTO security_user (user_login_name,user_password,user_salt) VALUES ('sales','sales',null);
INSERT INTO security_user (user_login_name,user_password,user_salt) VALUES ('products','products',null);
INSERT INTO security_user (user_login_name,user_password,user_salt) VALUES ('marketing','marketing',null);

INSERT INTO security_role (role_name,description) VALUES ('PRODUCTS_ROLE',null);
INSERT INTO security_role (role_name,description) VALUES ('MARKETING_ROLE',null);
INSERT INTO security_role (role_name,description) VALUES ('ADMIN_ROLE',null);
INSERT INTO security_role (role_name,description) VALUES ('SALES_ROLE',null);

INSERT INTO security_permission (permission_name,description) VALUES ('DELETE_RECORD',null);
INSERT INTO security_permission (permission_name,description) VALUES ('UPDATE_RECORD',null);
INSERT INTO security_permission (permission_name,description) VALUES ('CREATE_RECORD',null);
INSERT INTO security_permission (permission_name,description) VALUES ('READ_RECORD',null);

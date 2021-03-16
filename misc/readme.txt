========================Bigid Configuration Document========================================

Pre-requisites
•	JDK 1.8
•	Maven 3.0 or higher
•	Mysql server 5.7 or higher
•	Eclipse Luna/intellij Idea/STS

1.	Install JDK1.8 

	Download the JDK 1.8  based on your system 32bit/64bit and 
	Install it 
	Java download Url
 		http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
		http://www.wikihow.com/Install-the-Java-Software-Development-Kit
	Maven download url
		https://maven.apache.org/download.cgi

2.	Verify Java and Maven installation on your machine:
		Start->cmd java -version
 
		Start->cmd java -version
 

3.	how to set the class Path for Java and Maven:
	Computer->properties ->Advance System Settings ->Enviroment Varaible ->New
 
		For JAVA_HOME set both the value and click on OK
		Variable name : JAVA_HOME
		Variable value : C:\Program Files\Java\jdk1.8.0_6
		 
		For MAVEN_HOME set both value and click on OK
		Variable name : MAVEN_HOME
		Variable value : C:\Program Files\apache-maven-3.5.0
 
	Click on Path->Edit it
 		Variable name : Path
		Variable Value     C:\Program Files\Intel\WiFi\bin;C:\Program Files\Common Files\Intel\WirelessCommon;C:\Program Files (x86)\ESTsoft\ALZip;C:\Users\satish\AppData\Roaming\npm;%JAVA_HOME%\bin;%MAVEN_HOME%\bin


	 
4.	Clone the code from bitbucket:

	Git clone  https://satish_Wissen@bitbucket.org/wissentechnology/bigid.git

5.	Make a workspace in your system.
	Import Bigid Source Code into Eclipse:
	File -> New ->Java Project - >click on ‘Use default location’ -> Browse project location ‘bigid’ - > ok -> Next -> Finish

6.	Run the NPM Command:

	Go on tbid path > D:\Bigid\bigid\tbid ->type cmd and Enter
	Run all below npm command:
		npm install -g yo
		npm install -g bower
		npm install -g grunt-cli
		npm install
		npm install -g generator-karma generator-angular
		npm install angular-material –save
		bower install angular-material --save
		
		Build the npm : grunt build
		
	Check in tbid path "D:\Bigid\bigid\tbid", “dist” folder is genreated or not !!.
	If it “dist” folder is generated  Go on “message.properties” and change the “photos_upload_basedir” and “static_resource_basedir”

7.	Message.properties: 
		#image upload related properties
		photos_upload_basedir=D:\Bigid\bigid\bigid\tbid\dist
		#Base directory for static resources
		static_resource_basedir=D:\Bigid\bigid\bigid\tbid\dist


8.	Database setup:
	In Porject check in the MISC folder all the db related stuff.
	Install my>sql :   Create databse : 
		mysql>create database bigid;		          
		mysql>use bigid;
		mysql>show tables
		application.properties
			# DataSource settings
				spring.datasource.url = jdbc:mysql://localhost:3306/bigid
				spring.datasource.username = root
				spring.datasource.password = root
9.	How to run the application:
			Project>Run as >Maven clean
			Project>Run as >Maven install
			Project>src/main/java>com.bigid>
	WebApplication.java>Run as>Java application
	
10.	Create two folder on given Path : bigid\src\main\webapp\resources
	Images Profile Pic
	postImg -> post images

Link to mock-up:
https://www.justinmind.com/usernote/tests/22927639/23959918/23959933/index.html


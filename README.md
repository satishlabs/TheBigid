# The BIGID

This application is a breath of fresh air from existing social media; a site where users are less in charge of the things they consume, though they are in charge for categorizing and reflecting on content, such that the next user who comes along, may benefit from their previous engagement with a particular post.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purpose.

### Prerequisites

*List of softwares which are to be installed in the system for the application to run:*

```
1) JDK 1.8,  2) Maven 3.0 or higher,  3) MySQL 5.5 or higher,  4) nodejs,  5) GIT 2.9 or higher,6) Eclipse Mars or higher
```

### Installing / Configuring / Running

**Kindly follow the below steps in order to setup the workspace:**

```
1. _Git clone_ the project to your local system
2. *Create a Java Project* in Eclipse and import the *bigid* workspace
3. Point the project 'build path' to JDK 1.8
4. Convert the project to Maven project by configuring the project.(Right click 'project' -> Configure -> Convert to Maven Project..)
5. Edit the below entries in **messages.properties** file:
	```
	photos_upload_basedir
	static_resource_basedir
	```
	[Make sure these paths point to the dist folder (bigid\tbid\dist), it should be in the same '//' format as in the file.]
6. Configure the below entries in 'application.properties' file:
	a. spring.datasource.url = jdbc:mysql://localhost:3306/bigid
	b. spring.datasource.username = root
	c. spring.datasource.password = root
	d. spring.jpa.hibernate.ddl-auto = create
7. Run the below commands from command line path : 
 	_..bigid\tbid>npm install -g bower grunt-cli yo generator-karma generator-angular_
	_..bigid\tbid>bower install_
	_..bigid\tbid>grunt build_
8. Clean the project and then follow the below 2 steps for Maven build:
   a. Right click Project -> Run As -> Maven clean
   b. Right click Project -> Run As -> Maven install
9. Right click _WebApplication.java_ file and select _Run As -> Java Application_.
10. Once the build and the application is run, you are now ready to explore the application,hit the URL:
   _http://localhost:8080/static/index.html_
```

#### Steps for updating the files if any, from GIT


```
1. Please take latest _..\bigid\tbid\app_ folder from GIT and replace the folder in your workspace
2. Kindly make sure _..\bigid\tbid\Gruntfile.js_ & _..bigid\tbid\bower.json_ files are the latest from Git and run _bower install_ command from _..\bigid\tbid_ folder path.
3. Make sure you run the _grunt build_ command in the path : _..\bigid\tbid_ , please wait until it completes the build,which would update the *dist* folder to the latest files which were updated from Git.
4. After these steps, make sure your _bigid\src_ folder is updated with the latest git _src_ folder files.
5. Kindly take care of the entries in _messages.properties_ and _application.properties_ file as mentioned in **Installing** section.
6. Clean Project,Maven clean & build needs to be performed and then Run the _WebApplication_.
7. Clear browser cache/history if required (Ctrl+Shift+R in Google Chrome) and hit the URL.
```

## Deployment

Steps would be added soon.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Versioning

This is the first version and we are yet to come up with some improvements in future. 



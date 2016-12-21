====================================================

Meeting Calendar Application using Scala
Author: Upendra Bhandari
Application Version: 0.1
Scala-Version: 2.10.4

====================================================
Run Instructions:

1. Installation and Configuration
The program is compiled using Scala Built Tool (sbt). Please download from http://www.scala-sbt.org/download.html for your Operating system. For installation instructions refer http://www.scala-sbt.org/0.13/docs/Setup.html. Also ensure you can connect to web using command line or shell. In case your system is behind a proxy, configure proxy for sbt also.

Copy the the Application Source Folder i.e. MarketLogic to directory of choice. Inside the location "<full-path>\MarketLogic\src\org\marketlogic\meetingCalender", you will see source code files, target directory and project directory.

2. Add sbt-assembly to sbt: This step is already executed, For knowledge purpose only. Goto step 3
We use sbt-assembly plugin to create a fat jar consisting of application class plus all the dependecies needed to execute it. To download sbt-assembly plugin, create a "project" folder inside "<full-path>\MarketLogic\src\org\marketlogic\meetingCalender" and create a file assembly.sbt .Add following lines and save the file. 
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.2")

3. Compilation
Now compile the application, Run the following command at <full-path>\MarketLogic\src\org\marketlogic\meetingCalender
> sbt package
This will take some few minutes. The above command compile the scala files and creates JAR package target/scala-<version>/meeting-calendar_<scala-version>-<application version>. You can see the file size is small, since it contains only application files

4. Create complete package. Execute below command
> sbt assembly
This create another jar file inside target/scala-<version>/. Check the new jar created. Its file size will be greater than existing jar file. In this case, "Meeting Calendar-assembly-0.1.jar" is created.

5. The new jar can be executed as normal class file using java command. e.g. From src\org\marketlogic\meetingCalender, run below command
> java -jar "target\scala-2.10\Meeting Calendar-assembly-0.1.jar" c:\inputfile\sample1.txt

The Meeting Calendar application needs full path to input file as argument

6. To execute the application using only scala, run the following command at <path to src>\MarketLogic\src\org\marketlogic\meetCalender\
> sbt "run <path to input-file>"
The Meeting Calendar application expects full path to input file as an argument. 


===================================================

Library Dependecies:
sbt uses Apache Ivy to implement managed dependencies. build.sbt defines a Project, which holds a list of Scala expressions called settings. All library dependencies are included in build.sbt file. Please check below sample build.sbt file

name := "Meeting Calendar"
version := "0.1"
scalaVersion := "2.10.4"
libraryDependencies ++= Seq(
	"joda-time" % "joda-time" % "2.9.6",
	"org.joda"  % "joda-convert" % "1.6"
)

When we execute "sbt package", The name variable, scala-version and version is used as JAR name. In this case "meeting-calendar_2.10-0.1.jar"
# **Java Spring Task Manager**
Backend part of the task managing server.
###**Getting Started**

Install software that used in this application
####**Prerequisites**

To run this application you need to install:

    OpenJDK or Oracle JDK, version 1.8 or later
    Apache Maven
    PostgreSQL 9.6 and above

###**Installation**

Here is the instruction to run this server on your machine.

* Firstly, clone this repository

      $ git clone https://github.com/victimoftrap/todo-taskmanager.git

* Create your database

      $ sudo -u postgres psql
      postgres=# CREATE DATABASE eisetasks;
      postgres=# CREATE USER someuser WITH ENCRYPTED PASSWORD 'somepass';
      postgres=# GRANT ALL PRIVILEGES ON DATABASE eisetasks TO someuser;
      postgres=# \q

* Go to project directory and run

      $ mvn package

* Run project

      cd ./target/
      java -jar spring_homework-1.0-SNAPSHOT.jar

Now this server will start on port 8080
####**Usage**
The full API is available via link down below.
* [API] [1]

[1]: https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/6.0.0
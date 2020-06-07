# FileReader
Scan Path in Host Storage and get the files and folder information

* Build a Maven project with at least 2 modules
    * _core_ = contains services
    * _rest_ = rest api
* Build a Spring Boot Rest API
* run as self containing single Spring Boot jar with e.g. jetty or tomcat embedded
* Design a Service
    * reading/scanning all files and subfolders of a specific root folder (configure in
application.properties/yaml)
    * writing file metadata (path, filename, filetype=extension, filesize, modification date,
scan date=now) into a database (h2 is good enough)
    * run on startup
    * nice to have: scheduling all x minutes and rescan the folder and rewrite/update files
or drop and reinsert

* Rest ApI:
    * GET /folders
        * delivers a list of folders and subfolders sorted by name
    * GET /filesizes?ftype=xxx
        * delivers folders and subfolders with aggregated filesize sorted by size,
        * filtered by filetype/extension=xxx if avail

* Add some unit tests

You can use Spring Data, Hibernate, JDBC or any other framework.
Make as much as you can, build stubs or write some comments if you cannot solve something.

Good luck.
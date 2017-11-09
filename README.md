This is an example Maven project implementing an ImageJ 1.x plugin for a Institut Pasteur project.


To build the project, in the end you will have the `.jar` file
(called *artifact* in Maven speak) in the `target/` subdirectory.


* `git clone git://github.com/oliviermartinc/imagejcentrale`
* `cd imagejcentrale `
then to build the .jar file:
* `mvn package`
that needs to be installs to the plugins directory of Fiji


### Eclipse: To ensure that Maven copies the plugin to your ImageJ folder

1. Go to _Run Configurations..._
2. Choose _Maven Build_
3. Add the following parameter:
    - name: `imagej.app.directory`
    - value: `/path/to/ImageJ.app/`

This ensures that the final `.jar` file will also be copied to your ImageJ
plugins folder everytime you run the Maven Build

Play2.0 module for stateless authentication
===========================================================

This module adds stateless authentication features to Play2.0 applications.

Target
---------------------------------------

This module targets the __Scala__ version of __Play2.0__.

This module has been tested on Play2.0.1.

Motivation
---------------------------------------

### Stateless

### Simple

Installation
---------------------------------------

TODO...

1. add a repository resolver into your `Build.scala` or `build.sbt` file.

        pending sonatype...

1. add a dependency declaration into your `Build.scala` or `build.sbt` file.
    1. stable release

            "com.blendlabsinc" %% "play20.auth" % "0.1"

    1. current version

            "com.blendlabsinc" %% "play20.auth" % "0.1-SNAPSHOT"

For example: `Build.scala`

```scala
  val appDependencies = Seq(
    "com.blendlabsinc" %% "play20.auth" % "0.1"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers += ... pending sonatype
  )
```

Usage
---------------------------------------

See the sample application included in this git repository.


Attention
---------------------------------------

This module was based on play20-auth at https://github.com/t2v/play20-auth.

License
---------------------------------------

This library is released under the Apache Software License, version 2, 
which should be included with the source in a file named `LICENSE`.


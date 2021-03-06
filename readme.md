# AGFsApi4J - an alternative Gluster FS API for Java [![Build Status](https://travis-ci.org/agfsapi4j/agfsapi4j.svg?branch=master)](https://travis-ci.org/agfsapi4j/agfsapi4j) [![Test Coverage](https://codecov.io/gh/agfsapi4j/agfsapi4j/branch/master/graph/badge.svg)](https://codecov.io/gh/agfsapi4j/agfsapi4j) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.github.agfsapi4j:agfsapi4j)](https://sonarcloud.io/dashboard?id=com.github.agfsapi4j%3Aagfsapi4j) [![Maven Central](https://img.shields.io/maven-central/v/com.github.agfsapi4j/agfsapi4j.svg)](https://search.maven.org/#search%7Cga%7C1%7Cagfsapi4j) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

![Alternative Gluster FS API for Java](https://github.com/agfsapi4j/agfsapi4j/raw/master/doc/images/agfsapi4j-logo-egg-230x332.png "The alternative Bug")

#### An alternative GlusterFS API for Java

## Maven Dependency

```
<dependency>
  <groupId>com.github.agfsapi4j</groupId>
  <artifactId>agfsapi4j</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Comparison with original GlusterFS Java bindings
The original plan was the creation of a pure Java GlusterFS client lib. But this idea was discarded because of GlusterFS use 
of translators written in C that must be rewritten in Java too.

So AGFsApi4J is  - beside of the [original java binding](https://github.com/gluster/glusterfs-java-filesystem) -  
another GlusterFS java binding that depends on the native libgfapi, but uses [jna](https://github.com/java-native-access/jna)
for native access instead of jni.

Currently the features implemented are far from complete, but messages in case of errors are far, far better.

| Feature                     | AGFsApi4J | glusterfs-java-filesystem |
|:--------------------------- |:---------:|:-------------------------:|
| Depends on libgfapi         |    yes    |             yes           |
| Native access               |    jna    |             jni           |
| Java Filesystem integration |    no     |             yes           |
| Log Messages from libgfapi  |    yes    |              no           |

## Feature Status

| Feature                     |   Status   |
|:--------------------------- |:----------:|
| Connect                     |     yes    |
| Create file                 |     yes    |
| Read file                   |     yes    |
| Write file                  |     yes    |
| Delete file                 |     yes    |
| Rename file                 |     yes    |
| Create directory            |     yes    |
| Rename directory            |     yes    |
| Remove directory            |     yes    |
| List directory files        |    basic   |
| Get file stats              |    basic   |
| Get directory stats         |    basic   |
| Get cluster status          |     no     |
| Java filesytem integration  |     no     |
| ...                         |     no     |

## Example
```java
GlusterFsApi gfApi4J = GlusterFsApi.newInstance();
try (GlusterFsSession session = gfApi4J.connect("host", GlusterFsApi.DEFAULT_PORT, "vol0");)
{
    GlusterFsFile file = session.openFile(testFilePath, GlusterFsApi.O_RDONLY);
    byte[] buf = new byte[4096];
    int count = file.read(buf);
    file.close();
}
```

## Contributing
This is an open source project, and contributions are welcome! Feel free to raise an issue or submit a pull request.

## License
[Apache License, Version 2.0](license)

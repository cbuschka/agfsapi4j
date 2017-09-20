# AGFsApi4J - an alternative Gluster FS API for Java [![Build Status](https://travis-ci.org/agfsapi4j/agfsapi4j.svg?branch=master)](https://travis-ci.org/agfsapi4j/agfsapi4j) [![Test Coverage](https://codecov.io/gh/agfsapi4j/agfsapi4j/branch/master/graph/badge.svg)](https://codecov.io/gh/agfsapi4j/agfsapi4j) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

#### An alternative GlusterFS API for Java

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

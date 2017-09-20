package com.github.agfsapi4j;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CreateWriteReadIntegrationTest
{
	private static Random random = new Random();

	@Test
	public void testNode1() throws Exception
	{
		GlusterFsApi gfApi4J = GlusterFsApi.newInstance();
		try (GlusterFsSession session = gfApi4J.connect("knot1", GlusterFsApi.DEFAULT_PORT, "vol0");)
		{
			String cwd = session.cwd();
			assertThat(cwd, is("/"));

			String testFilePath = "/.test/test." + System.currentTimeMillis();
			GlusterFsFile file = session.create(testFilePath, GlusterFsApi.O_WRONLY, 0x755);
			byte[] testData = new byte[1024];
			random.nextBytes(testData);
			file.write(testData);
			file.close();

			file = session.open(testFilePath, GlusterFsApi.O_RDONLY);
			byte[] buf = new byte[4096];
			int count = file.read(buf);
			assertThat(count, is(testData.length));

			assertThat(Arrays.copyOf(buf, testData.length), is(testData));
			file.close();

			// session.truncate(testFilePath, 0);

			session.chown(testFilePath, 1000, 1000);

			String testDirPath = "/.test/testdir." + System.currentTimeMillis();
			session.mkdir(testDirPath, 0755);
			session.rename(testDirPath, testDirPath + ".2");
			session.rmdir(testDirPath+".2");
		}
	}
}

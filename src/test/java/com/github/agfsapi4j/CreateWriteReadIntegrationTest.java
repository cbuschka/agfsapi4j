package com.github.agfsapi4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CreateWriteReadIntegrationTest
{
	private static Random random = new Random();

	private String testNumber = "test" + Long.toHexString(System.currentTimeMillis()) + "_" + Long.toHexString(random.nextLong());

	private GlusterFsSession session;

	@Before
	public void setUp()
	{
		session = GlusterFsApi.newInstance().connect("knot1", GlusterFsApi.DEFAULT_PORT, "vol0");
	}

	@After
	public void tearDown()
	{
		session.close();
	}

	@Test
	public void mode() throws Exception
	{
		String basePath = "/.test/" + testNumber;
		testMode(0700);
		testMode(0070);
		testMode(0007);
		testMode(0500);
		testMode(01005);
		testMode(01234);
		testMode(01753);
	}

	@Test
	public void cwdAndMkdirAndChdir()
	{
		String testDirPath = testFile("cwdAndMkdirAndChdir");

		String cwd = session.cwd();
		assertThat(cwd, is("/"));

		session.mkdir(testDirPath, 0755);
		session.chdir(testDirPath);

		cwd = session.cwd();
		assertThat(cwd, is(testDirPath));
	}

	private String testFile(String suffix)
	{
		return "/.test/" + testNumber + "_" + suffix;
	}

	@Test
	public void writeAndStatsAndRead() throws Exception
	{
		String testFilePath = testFile("writeAndRead");

		GlusterFsFile file = session.create(testFilePath, GlusterFsApi.O_WRONLY, 0644);
		byte[] testData = new byte[1025];
		random.nextBytes(testData);
		file.write(testData);
		file.close();

		GlusterFsFileStats stat = session.stat(testFilePath);
		assertThat(stat.getSize(), is(1025));
		assertThat(stat.getUid(), is(1000));
		assertThat(stat.getGid(), is(1000));
		assertThat(Integer.toOctalString(stat.getMode()), is("644"));

		session.chown(testFilePath, stat.getUid(), stat.getGid());

		file = session.open(testFilePath, GlusterFsApi.O_RDONLY);
		byte[] buf = new byte[4096];
		int count = file.read(buf);
		assertThat(count, is(testData.length));

		assertThat(Arrays.copyOf(buf, testData.length), is(testData));
		file.close();

		session.unlink(testFilePath);
	}

	@Test
	public void mkdirAndRmdir()
	{
		String testDirPath = testFile("mkdirAndRmdir");

		session.mkdir(testDirPath, 0755);
		session.rename(testDirPath, testDirPath + ".2");
		session.rmdir(testDirPath + ".2");
	}

	private void testMode(int perms)
	{
		String filePath = testFile(Integer.toOctalString(perms));
		session.create(filePath, 0, perms);
		GlusterFsFileStats stat = session.stat(filePath);
		assertThat(Integer.toOctalString(stat.getMode()), is(Integer.toOctalString(perms)));

		session.unlink(filePath);
	}
}

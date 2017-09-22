package com.github.agfsapi4j;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CStringReaderTest
{
	@Test(expected = IndexOutOfBoundsException.class)
	public void empty()
	{
		String s = new CStringReader(10, "UTF-8").readFrom(ByteBuffer.wrap(new byte[]{}), 0);

		assertThat(s, is(""));
	}

	@Test
	public void zeroOnly()
	{
		String s = new CStringReader(10, "UTF-8").readFrom(ByteBuffer.wrap(new byte[]{0}), 0);

		assertThat(s, is(""));
	}

	@Test
	public void happyPath()
	{
		String s = new CStringReader(10, "UTF-8").readFrom(ByteBuffer.wrap(new byte[]{0, 65, 66, 0, 67}), 1);

		assertThat(s, is("AB"));
	}

	@Test(expected = IllegalStateException.class)
	public void limitReached()
	{
		String s = new CStringReader(2, "UTF-8").readFrom(ByteBuffer.wrap(new byte[]{0, 65, 66, 67, 0, 68}), 1);

		assertThat(s, is("AB"));
	}
}
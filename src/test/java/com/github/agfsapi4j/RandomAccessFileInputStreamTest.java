package com.github.agfsapi4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RandomAccessFileInputStreamTest
{
	private static final byte[] BUF = new byte[10];

	@Mock
	private RandomAccessFile randomAccessFile;

	private RandomAccessFileInputStream in;

	@Test
	public void closesIfRequested() throws IOException
	{
		in = new RandomAccessFileInputStream(randomAccessFile, true);

		in.close();

		verify(randomAccessFile).close();
		verifyNoMoreInteractions(randomAccessFile);
	}

	@Test
	public void doesntCcloseIfNotRequested() throws IOException
	{
		in = new RandomAccessFileInputStream(randomAccessFile, false);

		in.close();

		verify(randomAccessFile, never()).close();
		verifyNoMoreInteractions(randomAccessFile);
	}

	@Test
	public void delegateSingleByteRead() throws IOException
	{
		in = new RandomAccessFileInputStream(randomAccessFile, false);
		when(randomAccessFile.read()).thenReturn(42);

		int result = in.read();

		assertThat(result, is(42));
		verify(randomAccessFile).read();
		verifyNoMoreInteractions(randomAccessFile);
	}

	@Test
	public void delegateFullByteArrayRead() throws IOException
	{
		in = new RandomAccessFileInputStream(randomAccessFile, false);
		when(randomAccessFile.read(BUF, 0, BUF.length)).thenReturn(42);

		int result = in.read(BUF);

		assertThat(result, is(42));
		verify(randomAccessFile).read(BUF, 0, BUF.length);
		verifyNoMoreInteractions(randomAccessFile);
	}

	@Test
	public void delegatePartialByteArrayRead() throws IOException
	{
		in = new RandomAccessFileInputStream(randomAccessFile, false);
		when(randomAccessFile.read(BUF, 3, 6)).thenReturn(42);

		int result = in.read(BUF, 3, 6);

		assertThat(result, is(42));
		verify(randomAccessFile).read(BUF, 3, 6);
		verifyNoMoreInteractions(randomAccessFile);
	}

}
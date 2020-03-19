package com.carlosvin.covid.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class DownloadManager {

	public long download(InputStream inputStream, Path toFile) throws IOException {
		ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
		try (FileOutputStream fos = new FileOutputStream(toFile.toFile())) {
			return fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);			
		}
	}
}

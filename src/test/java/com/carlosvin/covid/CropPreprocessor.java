package com.carlosvin.covid;

import java.util.Arrays;

import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public final class CropPreprocessor implements OperationPreprocessor {
	private final int max;
	public CropPreprocessor(int max) {
		this.max = max;
	}
	public CropPreprocessor() {
		this(310);
	}

	@Override
	public OperationResponse preprocess(OperationResponse response) {
		byte[] content = response.getContent();
		if (content.length < max) {
			return response;
		}
		byte[] newArr = Arrays.copyOf(content, max);
		newArr[max-1] = newArr[max-2] = newArr[max-3] = '.';
		return new OperationResponseFactory().createFrom(response, newArr);
	}

	@Override
	public OperationRequest preprocess(OperationRequest request) {
		return request;
	}
	
	public static OperationPreprocessor crop() {
		return new CropPreprocessor();
	}
}

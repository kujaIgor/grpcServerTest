package com.example.grpc.services;

import java.util.ArrayList;
import java.util.List;

import helloWorld.HelloRequest;
import helloWorld.HelloResponse;
import helloWorld.HelloServiceGrpc.HelloServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloServiceImpl extends HelloServiceImplBase{

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloResponse> response) {
		String name = request.getName();
		
		HelloResponse reply = HelloResponse.newBuilder().setReply("Hello " + name).build();
		
		response.onNext(reply);
		response.onCompleted();
	}
	
	@Override
	public void lotsOfReplies(HelloRequest request, StreamObserver<HelloResponse> response) {
		String name = request.getName();
		int howMany = request.getHowMany();
		
		try {
			for (int i = 0; i < howMany; i++) {
				HelloResponse reply = HelloResponse.newBuilder().setReply("Hello " + name).build();
				
				Thread.sleep(800L);
				
				response.onNext(reply);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		response.onCompleted();
	}
	
	@Override 
	public StreamObserver<HelloRequest> lotsOfGreetings(StreamObserver<HelloResponse> response) {
		return new StreamObserver<HelloRequest>() {
			List<String> names = new ArrayList<String>();
			
			@Override
		    public void onNext(HelloRequest request) {
				names.add(request.getName());		     
		    }

		    @Override
		    public void onError(Throwable t) {
		    }

		    @Override
		    public void onCompleted() {
		    	String result = "";
		    	for (String name: names) {
		    		result = result + name + " ";
		    	}
		    	
		    	response.onNext(HelloResponse.newBuilder().setReply(result).setHowMany(names.size()).build());
		    	response.onCompleted();
		    }
		};
	}
	
	@Override 
	public StreamObserver<HelloRequest> bidiHello(StreamObserver<HelloResponse> response) {
		return new StreamObserver<HelloRequest>() {
			List<String> names = new ArrayList<String>();
			
			@Override
		    public void onNext(HelloRequest request) {
				try {
					Thread.sleep(8000L);
					names.add(request.getName());				
					String result = "";
			    	for (String name: names) {
			    		result = result + name + " ";
			    	}
			    	 
			    	response.onNext(HelloResponse.newBuilder().setReply(result).setHowMany(names.size()).build());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }

		    @Override
		    public void onError(Throwable t) {
		    }

		    @Override
		    public void onCompleted() {
		    	
		    	response.onCompleted();
		    }
		};
	}
}

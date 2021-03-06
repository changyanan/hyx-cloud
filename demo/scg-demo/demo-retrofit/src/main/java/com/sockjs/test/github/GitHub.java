package com.sockjs.test.github;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHub {
	
	@GET("/repos/{owner}/{repo}/contributors")
	List<Contributor> contributors(@Path("owner") String owner, @Path("repo") String repo );
}
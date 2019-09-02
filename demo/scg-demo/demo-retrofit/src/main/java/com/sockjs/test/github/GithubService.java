package com.sockjs.test.github;

import java.io.IOException;
import java.util.List;

import com.sockjs.test.utils.Utils;

import retrofit2.Retrofit;


public final class GithubService {
	public static final String API_URL = "https://api.github.com";

	public static void main( String... args) throws IOException {

		Retrofit retrofit = Utils.create(API_URL);

		GitHub github = retrofit.create(GitHub.class);

		List<Contributor> contributors = github.contributors("square", "retrofit");
		for (Contributor contributor : contributors) {
			System.out.println(contributor.login + " (" + contributor.contributions + ")");
		}
	}
}
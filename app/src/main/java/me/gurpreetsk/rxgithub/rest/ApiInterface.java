package me.gurpreetsk.rxgithub.rest;

import java.util.List;

import me.gurpreetsk.rxgithub.model.Issue;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Gurpreet on 15/05/17.
 */

public interface ApiInterface {

    @GET("repos/{org}/{repo}/issues")
    Observable<List<Issue>> getIssues(@Path("org") String organisation,
                                      @Path("repo") String repositoryName,
                                      @Query("page") int pageNumber);

}

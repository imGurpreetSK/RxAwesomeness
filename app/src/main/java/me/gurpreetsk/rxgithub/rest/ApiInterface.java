package me.gurpreetsk.rxgithub.rest;

import java.util.List;

import me.gurpreetsk.rxgithub.model.Comment;
import me.gurpreetsk.rxgithub.model.CommentBody;
import me.gurpreetsk.rxgithub.model.Issue;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("repos/{owner}/{repository}/issues/{number}/comments")
    @Headers({"Authorization : token 12054ee70d131a2706474dd88bc70ff3de76d607"
//            "Content-Type : application/json"
    })
    Call<Comment> addComment(@Path("owner") String owner,
                             @Path("repository") String repository,
                             @Path("number") String number,
                             @Field("body") String comment);

}

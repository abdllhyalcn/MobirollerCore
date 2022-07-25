package com.mobiroller.core.models.youtube;

import com.google.api.services.youtube.model.Video;

import java.io.Serializable;
import java.math.BigInteger;

public class YoutubeVideoDetailModel implements Serializable {

    String videoTitle;
    BigInteger videoViewCount;
    String videoId;
    BigInteger likeCount;
    BigInteger dislikeCount;
    BigInteger commentCount;
    String channelName;
    String channelId;
    String channelImageUrl;
    BigInteger channelSubscriberCount;
    String videoDescription;

    public String getVideoTitle() {
        return videoTitle;
    }

    public BigInteger getVideoViewCount() {
        return videoViewCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {
        this.likeCount = likeCount;
    }

    public void setDislikeCount(BigInteger dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public BigInteger getDislikeCount() {
        return dislikeCount;
    }

    public BigInteger getCommentCount() {
        return commentCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public BigInteger getChannelSubscriberCount() {
        return channelSubscriberCount;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideo(Video video)
    {
        videoId = video.getId();
        videoDescription = video.getSnippet().getDescription();
        videoTitle = video.getSnippet().getTitle();
        channelName = video.getSnippet().getChannelTitle();
        channelId = video.getSnippet().getChannelId();
        videoViewCount = video.getStatistics().getViewCount();
        likeCount = video.getStatistics().getLikeCount();
        dislikeCount = video.getStatistics().getDislikeCount();
        commentCount = video.getStatistics().getCommentCount();
    }

    public void setChannelImageUrl(String imageUrl)
    {
        channelImageUrl = imageUrl;
    }

    public void setChannelSubscriberCount(BigInteger subscriberCount)
    {
        channelSubscriberCount = subscriberCount;
    }
}

package com.mobiroller.core.models.youtube;


import com.google.api.services.youtube.model.Channel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BrandingSettings implements Serializable
{
    @SerializedName("channel")
    @Expose
    private Channel channel;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("hints")
    @Expose
    private List<Hint> hints = null;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }
}

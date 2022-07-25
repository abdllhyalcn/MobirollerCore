
package com.mobiroller.core.models.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Thumbnails implements Serializable {

    @SerializedName("default")
    @Expose
    private YoutubeImage _default;
    @SerializedName("medium")
    @Expose
    private YoutubeImage medium;
    @SerializedName("high")
    @Expose
    private YoutubeImage high;
    @SerializedName("standard")
    @Expose
    private YoutubeImage standard;
    @SerializedName("maxres")
    @Expose
    private YoutubeImage maxres;

    public YoutubeImage get_default() {
        return _default;
    }

    public void set_default(YoutubeImage _default) {
        this._default = _default;
    }

    public YoutubeImage getMedium() {
        return medium;
    }

    public void setMedium(YoutubeImage medium) {
        this.medium = medium;
    }

    public YoutubeImage getHigh() {
        return high;
    }

    public void setHigh(YoutubeImage high) {
        this.high = high;
    }

    public YoutubeImage getStandard() {
        return standard;
    }

    public void setStandard(YoutubeImage standard) {
        this.standard = standard;
    }

    public YoutubeImage getMaxres() {
        return maxres;
    }

    public void setMaxres(YoutubeImage maxres) {
        this.maxres = maxres;
    }

    public YoutubeImage getAvailableImage() {
        if (high != null)
            return high;
        else if (medium != null)
            return medium;
        else
            return _default;
    }
}

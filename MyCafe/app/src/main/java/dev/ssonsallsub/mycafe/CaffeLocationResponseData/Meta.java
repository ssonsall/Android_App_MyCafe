package dev.ssonsallsub.mycafe.caffelocationresponsedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("is_end")
    @Expose
    private Boolean isEnd;
    @SerializedName("pageable_count")
    @Expose
    private Integer pageableCount;
    @SerializedName("same_name")
    @Expose
    private Object sameName;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public Boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Integer getPageableCount() {
        return pageableCount;
    }

    public void setPageableCount(Integer pageableCount) {
        this.pageableCount = pageableCount;
    }

    public Object getSameName() {
        return sameName;
    }

    public void setSameName(Object sameName) {
        this.sameName = sameName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
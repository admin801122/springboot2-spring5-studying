package com.transaction.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "yy_comics")
public class Comics {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 漫画名称
     */
    private String name;

    /**
     * 描述
     */
    private String desciption;

    /**
     * 分类ID
     */
    @Column(name = "category_id")
    private String categoryId;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 是否完结
     */
    @Column(name = "is_end")
    private String isEnd;

    /**
     * 最近章节
     */
    @Column(name = "recent_chapter")
    private String recentChapter;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取漫画名称
     *
     * @return name - 漫画名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置漫画名称
     *
     * @param name 漫画名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return desciption - 描述
     */
    public String getDesciption() {
        return desciption;
    }

    /**
     * 设置描述
     *
     * @param desciption 描述
     */
    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    /**
     * 获取分类ID
     *
     * @return category_id - 分类ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 设置分类ID
     *
     * @param categoryId 分类ID
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取是否完结
     *
     * @return is_end - 是否完结
     */
    public String getIsEnd() {
        return isEnd;
    }

    /**
     * 设置是否完结
     *
     * @param isEnd 是否完结
     */
    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * 获取最近章节
     *
     * @return recent_chapter - 最近章节
     */
    public String getRecentChapter() {
        return recentChapter;
    }

    /**
     * 设置最近章节
     *
     * @param recentChapter 最近章节
     */
    public void setRecentChapter(String recentChapter) {
        this.recentChapter = recentChapter;
    }
}
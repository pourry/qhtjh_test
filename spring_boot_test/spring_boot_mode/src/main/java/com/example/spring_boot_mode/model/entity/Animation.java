package com.example.spring_boot_mode.model.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * ACG 收藏实体类（动画/漫画/小说/游戏 共用一张表）
 * 对应数据库 animation 表
 */
public class Animation {

    /** 主键ID */
    private String id;
    /** 名称 */
    private String name;
    /** 类型: animation/comic/novel/game */
    private String type;
    /** 地址（播放/阅读/下载链接或资源地址） */
    private String address;
    /** 备注说明 */
    private String notes;
    /** 别名/其他名称 */
    private String alias;
    /** 是否完结：yes-已完结、no-连载中 */
    private String hasend;
    /** 评分 0-10 */
    private BigDecimal rating;
    /** 标签，逗号分隔 */
    private String tags;

    // ====== 动画专属字段 ======
    /** 总集数 */
    private Integer episodes;
    /** 制作公司 */
    private String studio;
    /** 主要声优 */
    private String voiceActors;
    /** 原作来源: 漫改/轻改/原创/游戏改 */
    private String source;

    // ====== 漫画专属字段 ======
    /** 总话数 */
    private Integer chapters;
    /** 作者（漫画） */
    private String comicAuthor;
    /** 出版社 */
    private String publisher;
    /** 连载周期: 周刊/月刊/不定期 */
    private String serialization;

    // ====== 小说专属字段 ======
    /** 总字数（千字单位） */
    private Integer wordCount;
    /** 作者（小说） */
    private String novelAuthor;
    /** 平台（小说: 起点/晋江 等；游戏: 平台） */
    private String platform;
    /** 分类（小说: 玄幻/都市 等） */
    private String category;

    // ====== 游戏专属字段 ======
    /** 游戏平台: PC/PS5/Switch 等 */
    private String gamePlatform;
    /** 开发商 */
    private String developer;
    /** 游戏类型: RPG/ACT/AVG 等 */
    private String genre;
    /** 游玩时长（小时） */
    private Integer hoursPlayed;

    // ====== 系统字段 ======
    /** 封面图URL */
    private String pictureURL;
    /** 创建人ID（关联用户） */
    private String sscollector;
    /** 图片列表（关联查询） */
    private List<AnimationPictures> pictures;
    /** 创建时间 */
    private String createTime;
    /** 额外对象数据 */
    private Object object;
    /** 完结状态标签（显示用） */
    private String hasendLabel;
    /** 是否分享：true-分享到首页，false-仅自己可见 */
    private Boolean share;
    /** 分享时间 */
    private String shareTime;
    /** 是否开启消息提醒（前端传来的临时字段） */
    private Boolean remindopen;
    /** 提醒时间（前端传来的临时字段） */
    private String remindtime;
    /** 提醒消息（前端传来的临时字段） */
    private String remindmsg;
    /** 重复提醒类型（前端传来的临时字段） */
    private String repeatType;
    /** 自定义重复间隔（分钟，前端传来的临时字段） */
    private Integer repeatInterval;
    /** 周期结束时间（前端传来的临时字段） */
    private String repeatEndTime;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHasend() {
        return hasend;
    }

    public void setHasend(String hasend) {
        this.hasend = hasend;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getSscollector() {
        return sscollector;
    }

    public void setSscollector(String sscollector) {
        this.sscollector = sscollector;
    }

    public List<AnimationPictures> getPictures() {
        return pictures;
    }

    public void setPictures(List<AnimationPictures> pictures) {
        this.pictures = pictures;
    }

    public String getHasendLabel() {
        return hasendLabel;
    }

    public void setHasendLabel(String hasendLabel) {
        this.hasendLabel = hasendLabel;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public Boolean getRemindopen() {
        return remindopen;
    }

    public void setRemindopen(Boolean remindopen) {
        this.remindopen = remindopen;
    }

    /**
     * 处理FormData传递的字符串类型转换
     */
    public void setRemindopen(String remindopen) {
        if (remindopen != null && ("true".equalsIgnoreCase(remindopen) || "1".equals(remindopen))) {
            this.remindopen = Boolean.TRUE;
        } else {
            this.remindopen = Boolean.FALSE;
        }
    }

    public String getRemindtime() {
        return remindtime;
    }

    public void setRemindtime(String remindtime) {
        this.remindtime = remindtime;
    }

    public String getRemindmsg() {
        return remindmsg;
    }

    public void setRemindmsg(String remindmsg) {
        this.remindmsg = remindmsg;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    /**
     * 统一 setter，处理 FormData 传来的 String 和内部传的 Integer
     */
    public void setRepeatInterval(Object repeatInterval) {
        if (repeatInterval == null) {
            this.repeatInterval = null;
        } else if (repeatInterval instanceof Integer) {
            this.repeatInterval = (Integer) repeatInterval;
        } else if (repeatInterval instanceof Number) {
            this.repeatInterval = ((Number) repeatInterval).intValue();
        } else {
            // 字符串情况
            String s = repeatInterval.toString();
            if (!s.isEmpty()) {
                try {
                    this.repeatInterval = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    this.repeatInterval = null;
                }
            } else {
                this.repeatInterval = null;
            }
        }
    }

    public String getRepeatEndTime() {
        return repeatEndTime;
    }

    public void setRepeatEndTime(String repeatEndTime) {
        this.repeatEndTime = repeatEndTime;
    }

    // ====== 新增字段 getter/setter ======

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Integer getEpisodes() { return episodes; }
    public void setEpisodes(Integer episodes) { this.episodes = episodes; }

    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }

    public String getVoiceActors() { return voiceActors; }
    public void setVoiceActors(String voiceActors) { this.voiceActors = voiceActors; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Integer getChapters() { return chapters; }
    public void setChapters(Integer chapters) { this.chapters = chapters; }

    public String getComicAuthor() { return comicAuthor; }
    public void setComicAuthor(String comicAuthor) { this.comicAuthor = comicAuthor; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getSerialization() { return serialization; }
    public void setSerialization(String serialization) { this.serialization = serialization; }

    public Integer getWordCount() { return wordCount; }
    public void setWordCount(Integer wordCount) { this.wordCount = wordCount; }

    public String getNovelAuthor() { return novelAuthor; }
    public void setNovelAuthor(String novelAuthor) { this.novelAuthor = novelAuthor; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getGamePlatform() { return gamePlatform; }
    public void setGamePlatform(String gamePlatform) { this.gamePlatform = gamePlatform; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getHoursPlayed() { return hoursPlayed; }
    public void setHoursPlayed(Integer hoursPlayed) { this.hoursPlayed = hoursPlayed; }

    @Override
    public String toString() {
        return "Animation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", alias='" + alias + '\'' +
                ", hasend='" + hasend + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                '}';
    }
}

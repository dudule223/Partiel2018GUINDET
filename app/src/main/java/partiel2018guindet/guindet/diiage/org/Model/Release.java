package partiel2018guindet.guindet.diiage.org.Model;

import org.json.JSONObject;

public class Release {

    private String status;
    private String thumb;
    private String format;
    private String title;
    private String catno;
    private int year;
    private String resourceUrl;
    private String artist;
    private int id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Release() {
    }

    /**
     *
     * @param id
     * @param catno
     * @param title
     * @param status
     * @param year
     * @param artist
     * @param format
     * @param resourceUrl
     * @param thumb
     */
    public Release(String status, String thumb, String format, String title, String catno, int year, String resourceUrl, String artist, int id) {
        super();
        this.status = status;
        this.thumb = thumb;
        this.format = format;
        this.title = title;
        this.catno = catno;
        this.year = year;
        this.resourceUrl = resourceUrl;
        this.artist = artist;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatno() {
        return catno;
    }

    public void setCatno(String catno) {
        this.catno = catno;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Release build(JSONObject jsonObject)
    {
        Release r = new Release();
        try
        {
            if (jsonObject.has("status"))
            {
                r.setStatus(jsonObject.getString("status"));
            }
            if (jsonObject.has("thumb"))
            {
                r.setThumb(jsonObject.getString("thumb"));
            }
            if (jsonObject.has("format"))
            {
                r.setFormat(jsonObject.getString("format"));
            }
            if (jsonObject.has("title"))
            {
                r.setTitle(jsonObject.getString("title"));
            }
            if (jsonObject.has("catno"))
            {
                r.setCatno(jsonObject.getString("catno"));
            }
            if (jsonObject.has("year"))
            {
                r.setYear(jsonObject.getInt("year"));
            }
            if (jsonObject.has("resource_url"))
            {
                r.setResourceUrl(jsonObject.getString("resource_url"));
            }
            if (jsonObject.has("artist"))
            {
                r.setArtist(jsonObject.getString("artist"));
            }
            if (jsonObject.has("id"))
            {
                r.setId(jsonObject.getInt("id"));
            }
        }
        catch(Exception e)
        {

        }
        return r;
    }

}
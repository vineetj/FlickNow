package com.vineetjain.flicknow.ImageSearchModule.Model;


import java.util.ArrayList;

public class ImageListModel
{
    private String title;

    private ArrayList<SingleIImagetemModel> items;

    private String description;

    private String link;

    private String generator;

    private String modified;

    //// only in case of error
    private String stat;

    private int code;

    private String message;
    //////

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public ArrayList<SingleIImagetemModel> getItems ()
    {
        return items;
    }

    public void setItems (ArrayList<SingleIImagetemModel>items)
    {
        this.items = items;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getGenerator ()
    {
        return generator;
    }

    public void setGenerator (String generator)
    {
        this.generator = generator;
    }

    public String getModified ()
    {
        return modified;
    }

    public void setModified (String modified)
    {
        this.modified = modified;
    }



    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ImageListModel{" +
                "title='" + title + '\'' +
                ", items=" + items +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", generator='" + generator + '\'' +
                ", modified='" + modified + '\'' +
                ", stat='" + stat + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public class SingleIImagetemModel {


        private String tags;

        private String author;

        private String title;

        private String description;

        private String date_taken;

        private String link;

        private String author_id;

        private String published;

        private Media media;

        public String getTags ()
        {
            return tags;
        }

        public void setTags (String tags)
        {
            this.tags = tags;
        }

        public String getAuthor ()
        {
            return author;
        }

        public void setAuthor (String author)
        {
            this.author = author;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getDate_taken ()
        {
            return date_taken;
        }

        public void setDate_taken (String date_taken)
        {
            this.date_taken = date_taken;
        }

        public String getLink ()
        {
            return link;
        }

        public void setLink (String link)
        {
            this.link = link;
        }

        public String getAuthor_id ()
        {
            return author_id;
        }

        public void setAuthor_id (String author_id)
        {
            this.author_id = author_id;
        }

        public String getPublished ()
        {
            return published;
        }

        public void setPublished (String published)
        {
            this.published = published;
        }

        public Media getMedia ()
        {
            return media;
        }

        public void setMedia (Media media)
        {
            this.media = media;
        }



        public class Media
        {
            private String m;

            public String getM ()
            {
                return m;
            }

            public void setM (String m)
            {
                this.m = m;
            }


        }
    }
}


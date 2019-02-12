package carga.tcss450.uw.edu.phishapp.setlist;

import java.io.Serializable;

/**
 * Class to encapsulate a Phish.net Blog Post. Building an Object requires a publish date and title.
 *
 * Optional fields include URL, teaser, and Author.
 *
 *
 * @author Charles Bryan
 * @version 14 September 2018
 */
public class SetList implements  Serializable {

    private final String mLongDate;
    private final String mLocation;
    private final String mVenue;
    private final String mSetListData;
    private final String mSetListNotes;
    private final String mUrl;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private final String mLongDate;
        private final String mLocation;
        private  String mVenue = "";
        private  String mSetListData = "";
        private  String mSetListNotes = "";
        private  String mUrl = "";


        /**
         * Constructs a new Builder.
         *
         * @param longDate the published date of the blog post
         * @param location the title of the blog post
         */
        public Builder(String longDate, String location) {
            this.mLongDate = longDate;
            this.mLocation = location;
        }

        /**
         * Add an optional url for the full blog post.
         * @param venue an optional url for the full blog post
         * @return the Builder of this BlogPost
         */
        public Builder addVenue(final String venue) {
            mVenue = venue;
            return this;
        }

        /**
         * Add an optional url for the full blog post.
         * @param setListData an optional url for the full blog post
         * @return the Builder of this BlogPost
         */
        public Builder addSetListData(final String setListData) {
            mSetListData = setListData;
            return this;
        }

        /**
         * Add an optional teaser for the full blog post.
         * @param setListNotes an optional url teaser for the full blog post.
         * @return the Builder of this BlogPost
         */
        public Builder addSetListNotes(final String setListNotes) {
            mSetListNotes = setListNotes;
            return this;
        }

        /**
         * Add an optional author of the blog post.
         * @param url an optional author of the blog post.
         * @return the Builder of this BlogPost
         */
        public Builder addUrl(final String url) {
            mUrl = url;
            return this;
        }

        public SetList build() {
            return new SetList(this);
        }

    }


    private SetList(final Builder builder) {
        this.mLongDate = builder.mLongDate;
        this.mLocation = builder.mLocation;
        this.mVenue = builder.mVenue;
        this.mSetListData = builder.mSetListData;
        this.mSetListNotes = builder.mSetListNotes;
        this.mUrl = builder.mUrl;
    }


    public String getLongDate() { return mLongDate; }

    public String getLocation() {
        return mLocation;
    }

    public String getVenue() {
        return mVenue;
    }

    public String getSetListData() {
        return mSetListData;
    }

    public String getSetListNotes() {
        return mSetListNotes;
    }

    public String getUrl() {
        return mUrl;
    }

}

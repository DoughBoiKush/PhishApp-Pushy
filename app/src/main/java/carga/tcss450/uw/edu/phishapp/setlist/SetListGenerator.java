package carga.tcss450.uw.edu.phishapp.setlist;

public class SetListGenerator {

    public static final SetList[] SETLISTS;
    public static final int COUNT = 10;


    static {
        SETLISTS = new SetList[COUNT];
        for (int i = 0; i < SETLISTS.length; i++) {
            SETLISTS[i] = new SetList
                    .Builder("2016-10-03 12:59 pm",
                    "Seattle, Wa " + (i + 1))
                    .addVenue("Paramount")
                    .build();
        }
    }


    private SetListGenerator() { }


}

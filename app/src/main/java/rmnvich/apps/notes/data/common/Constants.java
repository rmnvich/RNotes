package rmnvich.apps.notes.data.common;

public class Constants {

    public static String LOG_TAG = "qwe";

    public static String DATABASE_NAME = "test_database" + 57;

    public static long DEFAULT_DELAY = 1000;
    public static int DEFAULT_COLOR = -16777216;

    public static String EXTRA_NOTE_ID = "note_id";
    public static String EXTRA_IMAGE_PATH = "image_path";
    public static String EXTRA_FAVORITE_NOTES = "is_favorite_notes";

    public static String EXTRA_REMINDER_ID = "reminder_id";
    public static String EXTRA_COMPLETED_REMINDERS = "is_completed_reminders";

    public static String KEY_ALL_NOTES = "all_notes";
    public static String KEY_IS_FAVORITE_NOTES = "favorite_notes";

    public static String KEY_ACTIVE_REMINDERS = "active_reminders";
    public static String KEY_COMPLETED_REMINDERS = "completed_reminders";

    public static int REPEAT_TYPE_ONCE = 0;
    public static int REPEAT_TYPE_HOURLY = 1;
    public static int REPEAT_TYPE_DAILY = 2;
    public static int REPEAT_TYPE_WEEKLY = 2;
    public static int REPEAT_TYPE_MONTHLY = 4;
    public static int REPEAT_TYPE_YEARLY = 5;

    public static int REQUEST_CODE_IMAGE = 0;
    public static int REQUEST_CODE_SHARE = 1;

    //TODO: Fix lock b in the note
    //TODO: Fix dialogs theme (need dark)
    //TODO: Fix StatusBar color in BottomSheetDialog
    //TODO: Protect notes with PIN, a few photos in note
    //TODO: Settings (vibrate, theme, pin, default color, lang)
}
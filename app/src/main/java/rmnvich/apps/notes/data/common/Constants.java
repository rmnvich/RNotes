package rmnvich.apps.notes.data.common;

public class Constants {

    public static String LOG_TAG = "qwe";

    public static String DATABASE_NAME = "test_database" + 58;

    public static long DEFAULT_DELAY = 500;
    public static int DEFAULT_COLOR = -13223103;

    public static String EXTRA_NOTE_ID = "note_id";
    public static String EXTRA_IMAGE_PATH = "image_path";
    public static String EXTRA_FAVORITE_NOTES = "is_favorite_notes";
    public static String EXTRA_LOCKED_NOTE = "is_locked_note";

    public static String EXTRA_REMINDER_ID = "reminder_id";
    public static String EXTRA_COMPLETED_REMINDERS = "is_completed_reminders";

    public static String KEY_PIN_EXISTS = "is_pin_exists";

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
    public static int REQUEST_CODE_PIN = 2;
    public static int REQUEST_CODE_UNLOCK_NOTE = 3;

    //TODO: Fix pin activity
    //TODO: Sections in RemindersFragment
    //TODO: Settings (vibrate on/off, theme, lang, pin, default color)
    //TODO: Optimize set data to adapters
}
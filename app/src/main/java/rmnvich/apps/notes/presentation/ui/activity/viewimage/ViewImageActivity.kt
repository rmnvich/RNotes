package rmnvich.apps.notes.presentation.ui.activity.viewimage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_image_activity.*
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.EXTRA_IMAGE_PATH
import java.io.File

class ViewImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_image_activity)
        setSupportActionBar(view_image_toolbar)
        view_image_toolbar.setNavigationOnClickListener { onBackPressed() }

        Glide.with(this)
            .load(File(intent.getStringExtra(EXTRA_IMAGE_PATH)))
            .into(image_photo_view)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}

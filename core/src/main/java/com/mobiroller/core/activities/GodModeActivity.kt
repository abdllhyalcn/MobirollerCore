package com.mobiroller.core.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.mobiroller.core.activities.base.AveActivity
import com.mobiroller.core.helpers.AppSettingsHelper
import com.mobiroller.core.helpers.godmode.GodMode
import com.mobiroller.core.helpers.godmode.SquidexAppData
import com.mobiroller.core.interfaces.ActivityComponent
import com.mobiroller.core.R

class GodModeActivity : AveActivity(), GodModeAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_god_mode)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        findViewById<TextView>(R.id.button_done).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.button_reset).setOnClickListener {
            AppSettingsHelper.setGodModeSelectedApp(null)
            showWarningDialog()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GodModeAdapter(godModeAdapterListener = this)

    }

    override fun injectActivity(component: ActivityComponent?): AppCompatActivity {
        return this
    }

    override fun showWarningDialog() {
        MaterialDialog.Builder(this)
            .title("Warning")
            .content("The app will now close. After rebooting the application, the settings will be applied.")
            .positiveText(getString(R.string.OK))
            .onPositive { _, _ ->
                finishAffinity()
            }
            .show()
    }

}

class GodModeAdapter(var godModeAdapterListener: GodModeAdapterListener? = null) : RecyclerView.Adapter<GodModeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GodModeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_god_mode_item, parent, false)
        return GodModeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GodModeViewHolder, position: Int) {
        holder.bind(model = GodMode.apps?.get(position), godModeAdapterListener = godModeAdapterListener)
    }

    override fun getItemCount(): Int {
       return GodMode.apps?.size ?: 0
    }

}

class GodModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: SquidexAppData?, godModeAdapterListener: GodModeAdapterListener?) {
        itemView.setOnClickListener {
            AppSettingsHelper.setGodModeSelectedApp(model)
            godModeAdapterListener?.showWarningDialog()
        }
        itemView.findViewById<TextView>(R.id.title_text_view).text = model?.data?.name
        itemView.findViewById<TextView>(R.id.description_text_view).text = model?.data?.description
        itemView.findViewById<ImageView>(R.id.checked_image_view).visibility =
            if (model?.id == AppSettingsHelper.getGodModeSelectedApp()?.id) View.VISIBLE else View.GONE
    }
}

interface GodModeAdapterListener {
    fun showWarningDialog()
}
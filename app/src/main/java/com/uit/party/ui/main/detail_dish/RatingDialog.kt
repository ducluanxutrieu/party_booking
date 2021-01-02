package com.uit.party.ui.main.detail_dish

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.uit.party.R


class RatingDialog(private val onListener: OnRatingDialogListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView: View = View.inflate(activity, R.layout.rating_dialog, null)
        dialogView.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            this.dismiss()
        }
        dialogView.setBackgroundResource(android.R.color.transparent)
        dialogView.findViewById<MaterialButton>(R.id.btn_submit).setOnClickListener {
            val reviewString =  dialogView.findViewById<TextInputEditText>(R.id.et_content_review).text
            val reviewScore =  dialogView.findViewById<RatingBar>(R.id.rating_bar).rating
            onListener.onSubmitted(reviewString.toString(), reviewScore)
            this.dismiss()
        }

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
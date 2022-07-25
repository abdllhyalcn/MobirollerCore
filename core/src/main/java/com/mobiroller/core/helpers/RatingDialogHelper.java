package com.mobiroller.core.helpers;

import android.content.Context;
import android.widget.Toast;

import com.mobiroller.core.R;
import com.mobiroller.core.views.dialogs.RatingDialog;

public class RatingDialogHelper {

    private static int THRESHOLD = 3;
    private static int SESSION_COUNT = 3;

    public static void checkRatingStatus(Context context) {
        if (UtilManager.networkHelper().isConnected() && UtilManager.sharedPrefHelper().getRateApp()) {
            final RatingDialog ratingDialog = new RatingDialog.Builder(context)
                    .title(context.getString(R.string.rating_dialog_experience))
                    .positiveButtonText(context.getString(R.string.rating_dialog_remind_later))
                    .negativeButtonText(context.getString(R.string.rating_dialog_never))
                    .formTitle(context.getString(R.string.rating_dialog_feedback_title))
                    .formHint(context.getString(R.string.rating_dialog_suggestions))
                    .formSubmitText(context.getString(R.string.rating_dialog_submit))
                    .formCancelText(context.getString(R.string.rating_dialog_cancel))
                    .threshold(THRESHOLD)
                    .session(SESSION_COUNT)
                    .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                        @Override
                        public void onFormSubmitted(String feedback, int rating) {
                            new ApiRequestManager().sendFeedback(feedback, rating);
                            Toast.makeText(context, context.getString(R.string.feedback_sent), Toast.LENGTH_SHORT).show();
                        }
                    }).build();
            ratingDialog.setCancelable(false);
            ratingDialog.setCanceledOnTouchOutside(false);
            ratingDialog.show();
        }
    }

}

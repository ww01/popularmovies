package pl.fullstack.movies.listener;

import android.view.View;

/**
 * Created by waldek on 11.07.17.
 */

public class ViewCoverListener implements View.OnClickListener {

    protected View container;

    public ViewCoverListener(View container) {
        this.container = container;
    }

    @Override
    public void onClick(View view) {
        this.container.setVisibility(View.GONE);
    }
}

package pl.fullstack.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.movies.fragment.LoginFragment;
import pl.fullstack.popularmovies.R;

/**
 * Created by waldek on 16.07.17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_content)
    protected ViewGroup container;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.login_activity);

        ButterKnife.bind(this);

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(this.container.getId(), new LoginFragment());
        ft.commit();
    }

}

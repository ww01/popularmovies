package pl.fullstack.movies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.popularmovies.R;

/**
 * Created by waldek on 16.07.17.
 */

public class LoginFragment extends Fragment {

    @BindView(R.id.login_name)
    protected EditText username;

    @BindView(R.id.login_password)
    protected EditText password;

    @BindView(R.id.login_submit)
    protected Button submitLogin;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle state){
        View view = inflater.inflate(R.layout.login_fragment, viewGroup, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
    }



}

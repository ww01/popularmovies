package pl.fullstack.movies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.popularmovies.R;
import pl.fullstack.security.Helper;

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

    @BindView(R.id.login_method_fb)
    protected LoginButton fbLoginButton;

    protected CallbackManager fbCallbackManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle state){
        View view = inflater.inflate(R.layout.login_fragment, viewGroup, false);
        ButterKnife.bind(this, view);

        this.fbLoginButton.setFragment(this);
        this.fbLoginButton.setReadPermissions("email");
        this.fbCallbackManager = CallbackManager.Factory.create();

        final Context context = this.getContext();

        this.fbLoginButton.registerCallback(this.fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Helper.setupLoginToken(context, loginResult.getAccessToken().getToken());
                Toast.makeText(context, R.string.login_attempt_ok, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, R.string.login_attempt_cancel, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(context, R.string.login_attempt_error, Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(this.fbCallbackManager != null)
            this.fbCallbackManager.onActivityResult(requestCode, resultCode, intent);
    }


}
